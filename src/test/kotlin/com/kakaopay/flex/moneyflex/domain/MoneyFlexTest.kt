package com.kakaopay.flex.moneyflex.domain

import com.kakaopay.flex.exception.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MoneyFlexTest {
    private val createdAt: LocalDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(3, 0))

    @Test
    fun `돈뿌리기 최소인원은 3명이다`() {
        // given, when, then
        assertThrows(UserCountException::class.java) {
            MoneyFlex(
                    token = "abc",
                    userId = 1234,
                    amount = BigDecimal(2),
                    userCount = 2,
                    roomId = "room-1"
            )
        }
    }

    @Test
    fun `뿌리는 돈은 참여자당 1원 보다 작으면 안된다`() {
        // given, when, then
        assertThrows(MinMoneyException::class.java) {
            MoneyFlex(
                    token = "abc",
                    userId = 1234,
                    amount = BigDecimal(2),
                    userCount = 3,
                    roomId = "room-1"
            )
        }
    }

    @Test
    fun `돈뿌리기 생성자는 돈을 받을 수 없다`() {
        // given, when
        val moneyFlex = MoneyFlex(
                token = "abc",
                userId = 1234,
                amount = BigDecimal(1000),
                userCount = 3,
                roomId = "room-1"
        )

        // then
        assertThrows(OwnerCanNotParticipateException::class.java) {
            moneyFlex.receiveMoney(
                    userId = 1234,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
        }
    }

    @Test
    fun `이미 참여한 사람은 돈을 받을 수 없다`() {
        // given, when
        val moneyFlex = MoneyFlex(
                token = "abc",
                userId = 1234,
                amount = BigDecimal(1000),
                userCount = 3,
                roomId = "room-1"
        ).also {
            it.receiveMoney(
                    userId = 12345,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
        }

        // then
        assertThrows(AlreadyParticipatedException::class.java) {
            moneyFlex.receiveMoney(
                    userId = 12345,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
        }
    }

    @Test
    fun `돈뿌리기를 모두 받은경우 참여할 수 없다`() {
        // given, when
        val moneyFlex = MoneyFlex(
                token = "abc",
                userId = 1234,
                amount = BigDecimal(1000),
                userCount = 3,
                roomId = "room-1"
        ).also {
            it.receiveMoney(
                    userId = 1,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
            it.receiveMoney(
                    userId = 2,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
            it.receiveMoney(
                    userId = 3,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
        }

        // then
        assertThrows(MoneyFlexIsFullException::class.java) {
            moneyFlex.receiveMoney(
                    userId = 4,
                    amount = BigDecimal(500),
                    LocalDateTime.now()
            )
        }
    }

    @Test
    fun `돈뿌리기 생성 후 10분이 지나면 받을 수 없다`() {
        // given, when
        val moneyFlex = MoneyFlex(
                token = "abc",
                userId = 1234,
                amount = BigDecimal(1000),
                userCount = 3,
                roomId = "room-1"
        ).also {
            it.createdAt = createdAt
        }
        val notReceivableDatetime = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(3, 11))

        // then
        assertThrows(MoneyFlexExpiredException::class.java) {
            moneyFlex.receiveMoney(
                    userId = 1234,
                    amount = BigDecimal(500),
                    notReceivableDatetime
            )
        }
    }

    @Test
    fun `돈뿌리기 생성 후 7일이 지나면 조회할 수 없다`() {
        // given, when
        val moneyFlex = MoneyFlex(
                token = "abc",
                userId = 1234,
                amount = BigDecimal(1000),
                userCount = 3,
                roomId = "room-1"
        ).also {
            it.createdAt = createdAt
        }
        val notReceivableDatetime = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(3, 11))

        // then
        assertTrue(moneyFlex.isNotReadable(notReceivableDatetime))
    }
}
