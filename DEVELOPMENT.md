# Development

## Prerequisites

- **Java 25** — e.g. `brew install openjdk@25` on macOS, or [Eclipse Temurin 25](https://adoptium.net/)
- Make sure `java` on your `PATH` is Java 25 (or set `JAVA_HOME`)

## Build

```bash
./gradlew shadowJar
```

Produces `build/libs/altibase-schema-diff.jar` — a fat JAR with all dependencies bundled.

## Test

```bash
./gradlew test
```

Uses JUnit 5. Test sources live under `src/test/java/`. CI runs tests on every push and PR via `.github/workflows/ci.yml`.

## Run locally

From a pre-built JAR (download from [Releases](https://github.com/f9n/altibase-schema-diff/releases)):

```bash
java -jar altibase-schema-diff.jar \
  --source-server <host1> \
  --target-server <host2> \
  --source-user <user> --source-password <password> \
  --target-user <user> --target-password <password>
```

From source after `./gradlew shadowJar`:

```bash
java -jar build/libs/altibase-schema-diff.jar \
  --source-server <host1> \
  --target-server <host2>
```

With environment variables:

```bash
export ALTIBASE_SOURCE_SERVER=<host1>
export ALTIBASE_SOURCE_USER=<user>
export ALTIBASE_SOURCE_PASSWORD=<password>
export ALTIBASE_TARGET_SERVER=<host2>
export ALTIBASE_TARGET_USER=<user>
export ALTIBASE_TARGET_PASSWORD=<password>

java -jar build/libs/altibase-schema-diff.jar
```

Or skip the JAR step entirely and run via Gradle:

```bash
./gradlew run --args='--source-server <host1> --target-server <host2>'
```

## Logging

Output goes to stderr. Use `--debug` or `--trace` for more detail:

```bash
java -jar build/libs/altibase-schema-diff.jar --debug \
  --source-server <host1> --target-server <host2>
```

| Flag | Level | What it shows |
|------|-------|---------------|
| *(default)* | INFO | Schema counts, extraction time |
| `--debug` | DEBUG | Table/procedure/sequence/view names, per-schema timing |
| `--trace` | TRACE | Every column detail |

## Releases

Push a version tag to trigger the release pipeline (`.github/workflows/release.yml`):

```bash
git tag v1.0.0 && git push origin v1.0.0
```

This builds the fat JAR, attaches it to a GitHub Release, and pushes the Docker image to [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry) as `ghcr.io/<owner>/altibase-schema-diff:<tag>`.
