package com.mysty.chefbook.api.profile.data.local.mappers

import com.mysty.chefbook.api.profile.data.local.dto.ProfileProto
import com.mysty.chefbook.api.profile.domain.entities.Profile
import com.mysty.chefbook.core.constants.Strings
import java.time.LocalDateTime
import java.time.ZoneOffset

internal fun Profile.toProto(): ProfileProto {
    return ProfileProto.newBuilder()
        .setId(id)
        .setEmail(email)
        .setUsername(username)
        .setCreationDate(creationDate.toEpochSecond(ZoneOffset.UTC))
        .setAvatar(avatar ?: Strings.EMPTY)
        .setPremium(premium)
        .setBroccoins(broccoins)
        .build()
}

internal fun ProfileProto.toEntity(): Profile {
    return Profile(
        id = id,
        email = email,
        username = username,
        creationDate = LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC),
        avatar = avatar.ifEmpty { null },
        premium = premium,
        broccoins = broccoins,
        isOnline = true,
    )
}
