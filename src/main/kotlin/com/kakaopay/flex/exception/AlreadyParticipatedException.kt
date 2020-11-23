package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class AlreadyParticipatedException : BaseException(HttpStatus.FORBIDDEN, "이미 돈뿌리기에 참가하였습니다.")