package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlin.math.max

interface RecipeMeta {
  val id: String

  val owner: ProfileInfo

  val visibility: Visibility
  val isEncryptionEnabled: Boolean

  val language: Language

  val version: Int
  val creationTimestamp: String?
  val updateTimestamp: String?

  val rating: Rating

  val tags: List<Tag>

  fun withId(id: String): RecipeMeta
  fun withScore(score: Int?): RecipeMeta
  fun withVersion(version: Int): RecipeMeta

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
      val newIndex = (lastIndex * lastVotes + scoreDiff) / max(newVotes, 1)

      return Rating(index = newIndex, score = score, votes = newVotes)
    }
  }
}
