#!/usr/bin/env python3
"""Run Sheppy's scripted UI cases from a small JSON file."""

import json
import pathlib
import subprocess
import sys


def main() -> int:
    if len(sys.argv) != 2:
        print("usage: run_ui_tests.py path/to/cases.json", file=sys.stderr)
        return 2

    root = pathlib.Path(__file__).resolve().parents[4]
    cases = json.loads(pathlib.Path(sys.argv[1]).read_text())
    subprocess.run([str(root / "gradlew"), "classes"], cwd=root, check=True)
    classes = root / "build/classes/java/main"

    data_file = root / "data/tasks.txt"
    saved_data = data_file.read_bytes() if data_file.exists() else None
    try:
        for case in cases:
            if data_file.exists():
                data_file.unlink()
            if "storage" in case:
                data_file.parent.mkdir(parents=True, exist_ok=True)
                data_file.write_text("\n".join(case["storage"]) + "\n")
            input_text = "\n".join(case["input"]) + "\n"
            result = subprocess.run(
                ["java", "-cp", str(classes), "sheppy.Sheppy"],
                cwd=root,
                input=input_text,
                text=True,
                capture_output=True,
                check=True,
            )
            print(f"--- {case['name']} ---")
            print(f"input: {input_text.rstrip()}")
            print(result.stdout, end="")
            missing = [line for line in case["expected"] if line not in result.stdout]
            if missing:
                print(f"FAIL: missing expected output: {missing}", file=sys.stderr)
                return 1
            print("PASS")
    finally:
        if saved_data is None:
            if data_file.exists():
                data_file.unlink()
        else:
            data_file.parent.mkdir(parents=True, exist_ok=True)
            data_file.write_bytes(saved_data)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
