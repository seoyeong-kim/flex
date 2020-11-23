package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class UserCountException(minUserCount: Int) : BaseException(HttpStatus.BAD_REQUEST, "돈뿌리기 최소인원은 ${minUserCount}명입니다.")