package com.cactusknights.chefbook.domain.entities.profile

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

data class Profile constructor(
    val id: String = UUID.randomUUID().toString(),
    val email: String? = null,
    val username: String? = null,
    val creationDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    val avatar: String? = null,
    val premium: Boolean = false,
    val broccoins: Int = 0,
    val isOnline: Boolean = false,
)
