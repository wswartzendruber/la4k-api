package org.la4k.impl

internal expect fun implementations(): List<Implementation>

public abstract class Implementation {

    public abstract fun getLogger(name: String): Logger
}