/*
 * Copyright 2020 William Swartzendruber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.la4k.test

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.la4k.Logger
import org.la4k.impl.Level
import org.la4k.proxy.action
import org.la4k.proxy.isDebugEnabled
import org.la4k.proxy.isErrorEnabled
import org.la4k.proxy.isFatalEnabled
import org.la4k.proxy.isInfoEnabled
import org.la4k.proxy.isTraceEnabled
import org.la4k.proxy.isWarnEnabled

class ApiTests {

    init {
        action = { name, level, message, throwable, tag ->
            messages.add(Message(name, level, message, throwable, tag))
        }
    }

    @Test
    fun `enabled message is logged`() {
        Logger("test-1").fatal("test-message-1")
        assertTrue(messages.any({
            it.name == "test-1" &&
            it.message == "test-message-1"
        }))
    }

    @Test
    fun `disabled message is not logged`() {
        isErrorEnabled = false
        Logger("test-2").error("test-message-2")
        assertFalse(messages.any({
            it.name == "test-2" &&
            it.message == "test-message-2"
        }))
    }

    @Test
    fun `disabled lambda is not logged`() {
        isErrorEnabled = false
        Logger("test-3").error({ fail("Lambda was evaluated.") })
    }

    @BeforeTest
    fun prepare() {
        messages.clear()
        isDebugEnabled = true
        isErrorEnabled = true
        isFatalEnabled = true
        isInfoEnabled = true
        isTraceEnabled = true
        isWarnEnabled = true
    }

    companion object {

        val messages = mutableListOf<Message>()

        data class Message(
            val name: String,
            val level: Level,
            val message: CharSequence,
            val throwable: Throwable?,
            val tag: String?
        )
    }
}
