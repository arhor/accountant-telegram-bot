package org.arhor.photospot.accountanttelegrambot.util

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import java.util.*

@Testable
internal class CoreUtilsKtTest {

    @Test
    fun `Head should return first element of the collection`() {
        // given
        val collection = listOf(1, 2, 3, 4, 5)

        // when
        val head = collection.head

        // then
        assertThat(head)
            .isEqualTo(collection[0])
    }

    @Test
    fun `Head should throw NoSuchElementException on empty collection`() {
        // given
        val collection = emptyList<Any>()

        // when
        val action = ThrowingCallable { collection.head }

        // then
        assertThatThrownBy(action)
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun `Tail should return all elements except first element`() {
        // given
        val collection = listOf(1, 2, 3, 4, 5)

        // when
        val tail = collection.tail

        // then
        assertThat(tail)
            .hasSize(collection.size - 1)
            .doesNotContain(collection[0])
            .containsAll(collection.drop(1))
    }

    @Test
    fun `Tail should return empty array on empty collection`() {
        // given
        val collection = emptyList<Any>()

        // when
        val tail = collection.tail

        // then
        assertThat(tail)
            .isEmpty()
    }
}