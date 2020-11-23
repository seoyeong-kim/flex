package com.kakaopay.flex.moneyflex.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MoneyFlexRepository : JpaRepository<MoneyFlex, String> {
    @EntityGraph(attributePaths = ["receivedMoneys"], type = EntityGraph.EntityGraphType.LOAD)
    fun findFirstByTokenAndRoomId(token: String, roomId: String): MoneyFlex?
}