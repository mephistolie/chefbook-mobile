package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.profile.ProfileInfo
import kotlin.math.max

data class RecipeMeta(
  val id: String,

  val owner: ProfileInfo,

  val visibility: Visibility = Visibility.PRIVATE,
  val isEncryptionEnabled: Boolean = false,

  val language: Language,

  val version: Int,
  val creationTimestamp: String? = null,
  val updateTimestamp: String? = null,

  val rating: Rating = Rating(),
) {

  fun withId(id: String) = copy(id = id)
  fun withScore(score: Int?) = copy(rating = rating.withScore(score))
  fun withVersion(version: Int) = copy(version = version)

  enum class Visibility {
    PRIVATE, LINK, PUBLIC;
  }

  data class Rating(
    val index: Float = 0F,
    val score: Int? = null,
    val votes: Int = 0,
  ) {

    fun withScore(score: Int?): Rating {
      val lastScore = this.score ?: 0
      val lastVotes = this.votes
      val lastIndex = this.index

      val newScore = score ?: 0

      val scoreDiff = newScore - lastScore
      val votesDiff = when {
        newScore != 0 && lastScore != 0 -> 0
        newScore != 0 -> 1
        else -> -1
      }

      val newVotes = max(lastVotes + votesDiff, 0)
      val newIndex = (lastIndex * lastVotes + scoreDiff) / newVotes

      return copy(index = newIndex, score = score, votes = newVotes)
    }
  }
}