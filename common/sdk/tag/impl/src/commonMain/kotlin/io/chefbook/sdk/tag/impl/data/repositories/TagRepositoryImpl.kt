package io.chefbook.sdk.tag.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.language.getSystemLanguageCode
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.internal.data.repositories.TagRepository
import io.chefbook.sdk.tag.impl.data.sources.local.LocalTagSource
import io.chefbook.sdk.tag.impl.data.sources.remote.RemoteTagSource
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TagRepositoryImpl(
  private val localSource: LocalTagSource,
  private val remoteSource: RemoteTagSource,

  scopes: CoroutineScopes,
  private val dispatchers: AppDispatchers,
) : TagRepository {

  private val loadTagsJob = scopes.repository.launch(start = CoroutineStart.LAZY) {
    remoteSource.getTags(language = getSystemLanguageCode()).onSuccess { localSource.cacheTags(it) }
  }

  override fun observeTags(): Flow<List<Tag>?> {
    loadTagsJob.start()
    return localSource.observeTags().distinctUntilChanged()
  }

  override suspend fun getTags(): List<Tag> = withContext(dispatchers.io) {
    loadTagsJob.join()
    return@withContext localSource.getTags().getOrNull() ?: emptyList()
  }
}
