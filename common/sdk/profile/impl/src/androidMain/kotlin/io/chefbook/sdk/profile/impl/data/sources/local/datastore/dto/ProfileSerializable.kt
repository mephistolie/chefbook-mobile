package io.chefbook.sdk.profile.impl.data.sources.local.datastore.dto

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.external.domain.entities.SubscriptionPlan
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSerializable(
  @SerialName("id")
  val id: String = Profile.LOCAL_PROFILE_ID,
  @SerialName("email")
  val email: String? = null,
  @SerialName("nickname")
  val nickname: String? = null,
  @SerialName("firstName")
  val firstName: String? = null,
  @SerialName("lastName")
  val lastName: String? = null,
  @SerialName("creationTimestamp")
  val creationTimestamp: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
  @SerialName("subscriptionPlan")
  val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
  @SerialName("broccoins")
  val broccoins: Int = 0,
  @SerialName("isOnline")
  val isOnline: Boolean = false,
)

fun ProfileSerializable.toEntity() =
  Profile(
    id = id,
    email = email,
    nickname = nickname,
    firstName = firstName,
    lastName = lastName,
    creationTimestamp = creationTimestamp,
    avatar = avatar,
    subscriptionPlan = subscriptionPlan,
    broccoins = broccoins,
    isOnline = isOnline
  )

fun Profile.toSerializable() =
  ProfileSerializable(
    id = id,
    email = email,
    nickname = nickname,
    firstName = firstName,
    lastName = lastName,
    creationTimestamp = creationTimestamp,
    avatar = avatar,
    subscriptionPlan = subscriptionPlan,
    broccoins = broccoins,
    isOnline = isOnline
  )
