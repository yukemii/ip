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
    classes = pathlib.Path("/tmp/sheppy-ui-classes")
    classes.mkdir(parents=True, exist_ok=True)
    sources = sorted((root / "src/main/java").glob("*.java"))
    subprocess.run(["javac", "-d", str(classes), *map(str, sources)],
                   cwd=root, check=True)

    for case in cases:
        input_text = "\n".join(case["input"]) + "\n"
        result = subprocess.run(
            ["java", "-cp", str(classes), "Sheppy"],
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
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
