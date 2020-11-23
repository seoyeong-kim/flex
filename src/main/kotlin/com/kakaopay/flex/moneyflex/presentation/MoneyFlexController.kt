package com.kakaopay.flex.moneyflex.presentation

import com.kakaopay.flex.moneyflex.application.MoneyFlexService
import com.kakaopay.flex.moneyflex.application.dto.CreateMoneyFlexResponse
import com.kakaopay.flex.moneyflex.application.dto.FindMoneyFlexResponse
import com.kakaopay.flex.moneyflex.application.dto.MoneyFlexRequest
import com.kakaopay.flex.moneyflex.application.dto.ReceiveMoneyResponse
import org.springframework.web.bind.annotation.*

@RestController
class MoneyFlexController(
        val moneyFlexService: MoneyFlexService
) {
    @PostMapping("v1/money-flex")
    fun createMoneyFlex(@RequestHeader(value = "X-USER-ID", required = true) userId: Long,
                        @RequestHeader(value = "X-ROOM-ID", required = true) roomId: String,
                        @RequestBody moneyFlexRequest: MoneyFlexRequest): CreateMoneyFlexResponse =
            moneyFlexService.flex(userId, roomId, moneyFlexRequest)


    @PostMapping("v1/money-flex/{token}/receive")
    fun receiveMoneyFlex(@RequestHeader(value = "X-USER-ID", required = true) userId: Long,
                         @RequestHeader(value = "X-ROOM-ID", required = true) roomId: String,
                         @PathVariable token: String): ReceiveMoneyResponse =
            moneyFlexService.receive(userId, roomId, token)


    @GetMapping("v1/money-flex/{token}")
    fun findMoneyFlex(@RequestHeader(value = "X-USER-ID", required = true) userId: Long,
                      @RequestHeader(value = "X-ROOM-ID", required = true) roomId: String,
                      @PathVariable token: String): FindMoneyFlexResponse =
            moneyFlexService.find(userId, roomId, token)
}