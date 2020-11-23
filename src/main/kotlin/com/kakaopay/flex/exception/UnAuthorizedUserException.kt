package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class UnAuthorizedUserException : BaseException(HttpStatus.FORBIDDEN, "해당 돈뿌리기를 조회할 수 없습니다.")