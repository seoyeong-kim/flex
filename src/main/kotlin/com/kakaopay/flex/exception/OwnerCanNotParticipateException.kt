package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class OwnerCanNotParticipateException : BaseException(HttpStatus.FORBIDDEN, "자신이 뿌린돈은 받을 수 없습니다.")