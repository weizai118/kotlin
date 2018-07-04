/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package test.random

import kotlin.random.Random
import kotlin.test.*

abstract class RandomSmokeTest {
    abstract val subject: Random

    @Test
    fun nextBits() {
        for (bitCount in 1..32) {
            val upperBitCount = 32 - bitCount
            var result = 0
            repeat(1000) {
                val bits = subject.nextBits(bitCount)
                result = result or bits

                assertEquals(0, bits.ushr(bitCount - 1).ushr(1), "Upper $upperBitCount bits should be zero")
            }

            assertEquals(1.shl(bitCount - 1).shl(1) - 1, result, "Lower $bitCount bits should be filled")
        }
    }


    @Test
    fun nextInt() {
        var result = 0
        repeat(10000) {
            result = result or subject.nextInt()
        }
        assertEquals(-1, result, "All bits should be filled")
    }

    @Test
    fun nextIntBound() {
        assertFailsWith<IllegalArgumentException> { subject.nextInt(0) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(-1) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(Int.MIN_VALUE) }

        repeat(1000) {
            assertEquals(0, subject.nextInt(1))
        }

        for (bound in listOf(2, 3, 7, 16, 32, 0x4000_0000, Int.MAX_VALUE)) {
            repeat(1000) {
                val x = subject.nextInt(bound)
                if (x !in 0 until bound)
                    fail("Value $x must be in range [0, $bound)")
            }
        }
    }

    @Test
    fun nextIntOriginBound() {
        assertFailsWith<IllegalArgumentException> { subject.nextInt(0, 0) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(-1, -2) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(Int.MIN_VALUE, Int.MIN_VALUE) }

        repeat(1000) { n ->
            assertEquals(n, subject.nextInt(n, n + 1))
        }

        for ((origin, bound) in listOf((0 to 2), (-1 to 5), (0 to 32), (0 to Int.MAX_VALUE), (-1 to Int.MAX_VALUE), (Int.MIN_VALUE to Int.MAX_VALUE))) {
            repeat(1000) {
                val x = subject.nextInt(origin, bound)
                if (x !in origin until bound)
                    fail("Value $x must be in range [$origin, $bound)")
            }
        }
    }

    @Suppress("EmptyRange")
    @Test
    fun nextIntInIntRange() {
        assertFailsWith<IllegalArgumentException> { subject.nextInt(0 until 0) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(-1..Int.MIN_VALUE) }
        assertFailsWith<IllegalArgumentException> { subject.nextInt(Int.MAX_VALUE until Int.MAX_VALUE) }

        repeat(1000) { n ->
            assertEquals(n, subject.nextInt(n..n))
        }

        for (range in listOf((0 until 2), (-1 until 5), (0 until 32), (0 until Int.MAX_VALUE),
                             (0..Int.MAX_VALUE), (Int.MIN_VALUE..0), (Int.MIN_VALUE..Int.MAX_VALUE))) {
            repeat(1000) {
                val x = subject.nextInt(range)
                if (x !in range)
                    fail("Value $x must be in range $range")
            }
        }
    }

    @Test
    fun nextLong() {
        var result = 0L
        repeat(10000) {
            result = result or subject.nextLong()
        }
        assertEquals(-1, result, "All bits should be filled")
    }

    @Test
    fun nextLongBound() {
        assertFailsWith<IllegalArgumentException> { subject.nextLong(0) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(-1) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(Long.MIN_VALUE) }

        repeat(1000) {
            assertEquals(0, subject.nextLong(1))
        }

        for (bound in listOf(2, 23, 32, 0x1_0000_0000, 0x4000_0000_0000_0000, Long.MAX_VALUE)) {
            repeat(1000) {
                val x = subject.nextLong(bound)
                if (x !in 0 until bound)
                    fail("Value $x must be in range [0, $bound)")
            }
        }
    }

    @Test
    fun nextLongOriginBound() {
        assertFailsWith<IllegalArgumentException> { subject.nextLong(0, 0) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(-1, -2) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(Long.MIN_VALUE, Long.MIN_VALUE) }

        repeat(1000) { i ->
            val n = 0x1_0000_0000 - 500 + i
            assertEquals(n, subject.nextLong(n, n + 1))
        }

        for ((origin, bound) in listOf((0L to 32L), (-1L to 5L), (0L to 0x1_0000_0000),
                                       (0L to Long.MAX_VALUE), (-1L to Long.MAX_VALUE), (Long.MIN_VALUE to Long.MAX_VALUE))) {
            repeat(1000) {
                val x = subject.nextLong(origin, bound)
                if (x !in origin until bound)
                    fail("Value $x must be in range [$origin, $bound)")
            }
        }
    }

    @Suppress("EmptyRange")
    @Test
    fun nextLongInLongRange() {
        assertFailsWith<IllegalArgumentException> { subject.nextLong(0L until 0L) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(-1..Long.MIN_VALUE) }
        assertFailsWith<IllegalArgumentException> { subject.nextLong(Long.MAX_VALUE until Long.MAX_VALUE) }

        repeat(1000) { i ->
            val n = 0x1_0000_0000 - 500 + i
            assertEquals(n, subject.nextLong(n..n))
        }

        for (range in listOf((0L until 2L), (-1L until 5L), (0L until 32L), (0L until Long.MAX_VALUE),
                             (0L..Long.MAX_VALUE), (Long.MIN_VALUE..0), (Long.MIN_VALUE..Long.MAX_VALUE))) {
            repeat(1000) {
                val x = subject.nextLong(range)
                if (x !in range)
                    fail("Value $x must be in range $range")
            }
        }
    }

}


class DefaultRandomSmokeTest : RandomSmokeTest() {
    override val subject: Random get() = Random
}

class SeededRandomSmokeTest : RandomSmokeTest() {
    override val subject: Random get() = Random(Random.nextInt())
}