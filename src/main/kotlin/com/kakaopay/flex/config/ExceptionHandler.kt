package com.kakaopay.flex.config

import com.kakaopay.flex.exception.BaseException
import org.apache.tomcat.util.ExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandler {
    companion object : Log {
        private const val DEFAULT_MESSAGE = "시스템 문제가 발생했습니다.\n 잠시후 다시 시도해 주세요."

    }

    @ExceptionHandler(value = [BaseException::class])
    private fun baseException(request: HttpServletRequest, e: BaseException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.message!!), e.status)
    }

    @ExceptionHandler(value = [Exception::class])
    private fun internalError(request: HttpServletRequest, e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("url:${request.requestURL}, ${e.message}")

        return ResponseEntity(ErrorResponse(DEFAULT_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}