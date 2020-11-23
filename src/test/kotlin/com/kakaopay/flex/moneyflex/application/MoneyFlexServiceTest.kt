package com.kakaopay.flex.moneyflex.application

import com.kakaopay.flex.exception.MoneyFlexNotFoundException
import com.kakaopay.flex.exception.UnAuthorizedUserException
import com.kakaopay.flex.moneyflex.domain.MoneyFlex
import com.kakaopay.flex.moneyflex.domain.MoneyFlexRepository
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@SpringBootTest
class MoneyFlexServiceTest {
    @Autowired
    private lateinit var moneyFlexService: MoneyFlexService

    @Autowired
    private lateinit var moneyFlexRepository: MoneyFlexRepository

    @Before
    fun setUp() {
        // given
        moneyFlexRepository.save(
                MoneyFlex(
                        token = "abc",
                        userId = 1234,
                        amount = BigDecimal(1000),
                        userCount = 3,
                        roomId = "room-1"
                )
        )
    }

    @Test
    fun `조회시 잘못된 roomId와 token을 입력하면 Exception 발생`() {
        // when, then
        assertThrows(MoneyFlexNotFoundException::class.java) {
            moneyFlexService.find(
                    userId = 1234,
                    roomId = "1234",
                    token = "abc"
            )
        }
        assertThrows(MoneyFlexNotFoundException::class.java) {
            moneyFlexService.find(
                    userId = 1234,
                    roomId = "room-1",
                    token = "abcd"
            )
        }
    }

    @Test
    fun `생성자가 아닌 사용자 조회시 Exception 발생`() {
        assertThrows(UnAuthorizedUserException::class.java) {
            moneyFlexService.find(
                    userId = 12344,
                    roomId = "room-1",
                    token = "abc"
            )
        }
    }
}