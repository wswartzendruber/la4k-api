# Introduction

This is a logging API for Kotlin Multiplatform. In principle, `la4k-api` is combined with one
or more bridges. Each bridge is responsible for forwarding logging events from the API to an
actual logging system. Each event will be sent across any bridges that are available. If no
bridges are present, then the event will simply be discarded.

Currently, only the Kotlin/JVM target is supported. Kotlin/JS and Kotlin/Native are planned for
future releases.

# For Libraries

Library authors should use `la4k-api` and only that for dispatching logging events within their
components. Libraries should **never** reference LA4K bridges directly.

To use `la4k-api` in your library, add it to your project, and then import the `org.la4k.logger`
function:

```kotlin
import org.la4k.logger
```

Instanciate your loggers like so:

```kotlin
val log = logger("my.logger.name")
```

There are six logging levels: FATAL, ERROR, WARN, INFO, DEBUG, and TRACE. Each logging statement
must include a message and may optionally include an exception and/or a tag of some kind. If a
message takes a long amount of time to evaluate, it may be passed in as a lambda instead.

```kotlin
log.fatal("This is a simple message.")
```

```kotlin
log.error("This has a message with a caught exception.", aCaughtException)
```

```kotlin
log.warn("This has a message, an exception, and a tag.", aCaughtException, "AN_ARBITRARY_TAG")
```

```kotlin
log.info {
    someTimeIntensiveOperation()
    "This message takes a while to evaluate, so it only happens if the INFO level is enabled."
}
```

```kotlin
log.debug(aCaughtException) {
    someTimeIntensiveOperation()
    "Like above, but includes a caught exception."
}
```

```kotlin
log.trace(aCaughtException, "AN_ARBITRARY_TAG") {
    someTimeIntensiveOperation()
    "Like above, but is only evaluated if TRACE is enabled for the provided tag."
}
```

# For Applications

If an application has a dependency that uses `la4k-api`, then a bridge needs to be imported into
the application.

## Kotlin/JVM

Bridges for the JVM use the Java Services API to register themselves for `la4k-api` to find. As
such, these bridges only need to be in the classpath during runtime and no other configuration
needs to be performed for them to be activated.

### Apache Log4j

The `la4k-log4j2` bridge connects `la4k-api` to the excellent Apache Log4j engine. Only version
2 is supported as version 1 has been discontinued.

As LA4K's levels were modeled after those from Log4j, no level conversion takes place.

LA4K tags become standalone Log4j markers, which are cached for each `org.la4k.Logger` instance
by this bridge.

### Java Logging

The `la4k-jul` bridge connects `la4k-api` to `java.util.logging`.

The following level mappings are used:

| LA4K  | JUL     |
|-------|---------|
| FATAL | SEVERE  |
| ERROR | SEVERE  |
| WARN  | WARNING |
| INFO  | INFO    |
| DEBUG | FINE    |
| TRACE | FINER   |

As standard Java logging has no concept of tags or markers, they are ignored. Any query for a
level being enabled for a specific tag returns `true` as long as that level is enabled for the
logger in question.

### SLF4J

The `la4k-slf4j` bridge connects `la4k-api` to SLF4J, and therefore, to Logback (if desired).

The following level mappings are used:

| LA4K  | SLF4J |
|-------|-------|
| FATAL | ERROR |
| ERROR | ERROR |
| WARN  | WARN  |
| INFO  | INFO  |
| DEBUG | DEBUG |
| TRACE | TRACE |

LA4K tags become SLF4J markers, which are cached for each `org.la4k.Logger` instance by this
bridge.

# Licensing

This project is made available under version 2.0 of the Mozilla Public License. As this license
is somewhat uncommon, it may be unfamiliar to some people. Simply put, anyone is free to use
this project as-is, to include static linking and even compiling this code in with proprietary
code. Any changes to any of this project's files that are distributed outside of an organization
must be made available under the same terms. This license is **not** viral.