package com.cactusknights.chefbook.domain.entities.profile

import java.time.LocalDateTime

data class Profile constructor(
    val id: Int = 0,
    val email: String? = null,
    val username: String? = null,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val avatar: String? = null,
    val premium: Boolean = false,
    val broccoins: Int = 0,
    val isOnline: Boolean = false,
)
