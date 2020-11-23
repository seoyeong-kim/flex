package com.kakaopay.flex.moneyflex.application

import com.kakaopay.flex.exception.MoneyFlexExpiredException
import com.kakaopay.flex.exception.MoneyFlexNotFoundException
import com.kakaopay.flex.exception.UnAuthorizedUserException
import com.kakaopay.flex.moneyflex.application.dto.CreateMoneyFlexResponse
import com.kakaopay.flex.moneyflex.application.dto.FindMoneyFlexResponse
import com.kakaopay.flex.moneyflex.application.dto.MoneyFlexRequest
import com.kakaopay.flex.moneyflex.application.dto.ReceiveMoneyResponse
import com.kakaopay.flex.moneyflex.domain.MoneyFlex
import com.kakaopay.flex.moneyflex.domain.MoneyFlexRepository
import com.kakaopay.flex.support.TokenGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime


@Service
class MoneyFlexService(
        val moneyFlexRepository: MoneyFlexRepository
) {
    @Transactional
    fun flex(userId: Long, roomId: String, moneyFlexRequest: MoneyFlexRequest): CreateMoneyFlexResponse {
        val moneyFlex = moneyFlexRepository.save(
                MoneyFlex(
                        token = TokenGenerator.generate(),
                        userId = userId,
                        amount = moneyFlexRequest.amount,
                        userCount = moneyFlexRequest.userCount,
                        roomId = roomId
                )
        )

        return CreateMoneyFlexResponse(moneyFlex.token)
    }

    @Transactional
    fun receive(userId: Long, roomId: String, token: String): ReceiveMoneyResponse {
        val moneyFlex = moneyFlexRepository.findFirstByTokenAndRoomId(token, roomId)
                ?: throw MoneyFlexNotFoundException()

        val splitMoney = moneyFlex.selectSplitMoney(token, roomId, userId)
        val receivedMoney = moneyFlex.receiveMoney(
                userId = userId,
                amount = splitMoney.amount,
                compareDateTime = LocalDateTime.now()
        )
        moneyFlexRepository.save(moneyFlex)

        return ReceiveMoneyResponse(receivedMoney.amount)
    }

    @Transactional(readOnly = true)
    fun find(userId: Long, roomId: String, token: String): FindMoneyFlexResponse {
        val moneyFlex = moneyFlexRepository.findFirstByTokenAndRoomId(token, roomId)
                ?: throw MoneyFlexNotFoundException()
        if (moneyFlex.isNotOwner(userId)) {
            throw UnAuthorizedUserException()
        }
        if (moneyFlex.isNotReadable(LocalDateTime.now())) {
            throw MoneyFlexExpiredException()
        }

        val receivedMoneys = moneyFlex.receivedMoneys
                .map {
                    FindMoneyFlexResponse.ReceivedMoneyResponse(
                            amount = it.amount,
                            userId = it.userId
                    )
                }
        val receivedTotalMoney = receivedMoneys
                .map { it.amount }
                .fold(BigDecimal.ZERO, BigDecimal::add)

        return FindMoneyFlexResponse(
                createdAt = moneyFlex.createdAt,
                amount = moneyFlex.amount,
                receivedAmount = receivedTotalMoney,
                receivedMoneys = receivedMoneys
        )
    }
}