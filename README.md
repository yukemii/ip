# Sheppy

Sheppy is a small command-line task manager written in Java 25.

## Running with Gradle

This project uses Gradle's standard Java layout. The application entry point is
`sheppy.Sheppy`.

Use the included Gradle Wrapper so the project downloads and uses the pinned
Gradle version automatically:

```bash
./gradlew build
./gradlew run
```

The `run` task keeps the terminal connected to Sheppy's input. Type commands
such as `list` or `bye` as usual.

Useful tasks include:

```bash
./gradlew tasks     # list available tasks
./gradlew build     # compile, test, and assemble the project
./gradlew test      # run unit tests
./gradlew jar       # create build/libs/sheppy-1.0.jar
./gradlew clean     # remove generated build output
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

Gradle uses the Java 25 toolchain configured in `build.gradle`. IntelliJ and
VS Code can both import and run the same Gradle project; the IDE only provides
a graphical way to invoke the Gradle tasks.

The standard Gradle source directories are:

```text
src/main/java       application source
src/test/java       unit tests
```

The generated `build/` directory contains compiled classes and the JAR and is
ignored by Git. The `gradle/wrapper/` files and `gradlew` scripts are committed
so that everyone uses the same Gradle version.

## Creating and running the executable JAR

Create the JAR with:

```bash
./gradlew jar
```

Gradle places it at `build/libs/sheppy-1.0.jar`. To run it directly:

```bash
java -jar build/libs/sheppy-1.0.jar
```

The JAR can also be copied into an empty folder and run there. Sheppy will
create its relative `data/tasks.txt` file in that folder when it saves tasks.

## Coding conventions

The Java source follows the SE-EDU basic and intermediate coding conventions:

- Use four spaces for indentation, not tabs.
- Keep lines at 120 characters or fewer.
- Use PascalCase for classes and lowerCamelCase for methods and variables.
- Use braces for control-flow blocks.
- Add Javadoc to public classes and methods and to non-obvious fields.

The `.editorconfig` file records the formatting rules for editors that support
EditorConfig, including VS Code.
