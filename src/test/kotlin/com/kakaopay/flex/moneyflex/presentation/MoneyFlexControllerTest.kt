package com.kakaopay.flex.moneyflex.presentation

import com.kakaopay.flex.moneyflex.application.dto.CreateMoneyFlexResponse
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal


class MoneyFlexControllerTest: BaseControllerTest() {

    @Test
    fun `돈 뿌리기 생성 성공`(){
        //given
        val userId: Long = 1234
        val roomId = "room1"
        val amount = BigDecimal(1000)
        val userCount = 3

        //when
        val result = createMoneyFlex(userId, roomId, amount, userCount)

        //then
        result.andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("token", hasLength(3)))
    }

    @Test
    fun `받기 성공`() {
        //given
        // 받기를 수행할 MoneyFlex 생성
        val userId: Long = 1234
        val roomId = "room1"
        val amount = BigDecimal(1000)
        val userCount = 3

        val token = this.objectMapper
                .readValue(
                        createMoneyFlex(userId, roomId, amount, userCount)
                                .andReturn().response.contentAsString,
                        CreateMoneyFlexResponse::class.java
                ).token

        //when
        val result = this.mockMvc.perform(
                post("/v1/money-flex/{token}/receive", token)
                        .header("X-USER-ID", 2345)
                        .header("X-ROOM-ID", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )

        //then
        result.andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("amount", notNullValue()))
    }

    @Test
    fun `돈 뿌리기 조회`() {
        //given
        val userId: Long = 1234
        val roomId = "room1"
        val amount = BigDecimal(1000)
        val userCount = 3

        val responseString = createMoneyFlex(userId, roomId, amount, userCount)
                .andReturn()
                .response
                .contentAsString

        val token = this.objectMapper
                .readValue(responseString, CreateMoneyFlexResponse::class.java)
                .token

        //when
        val result = findMoneyFlex(userId, roomId, token)

        //then
        result.andExpect(status().isOk)
                .andDo(print())
    }

    private fun createMoneyFlex(userId: Long, roomId: String, amount: BigDecimal, userCount: Int): ResultActions {
        val body = """
            {
                "amount": $amount,
                "userCount": $userCount
            }
        """.trimIndent()

        return this.mockMvc.perform(
                post("/v1/money-flex")
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
        )
    }

    private fun findMoneyFlex(userId: Long, roomId: String, token: String): ResultActions {
        return this.mockMvc.perform(
                get("/v1/money-flex/{token}", token)
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
    }
}