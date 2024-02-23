package io.chefbook.sdk.tag.impl.data.sources.remote

import io.chefbook.sdk.tag.api.external.domain.entities.Tag

internal interface RemoteTagSource {

  suspend fun getTags(language: String? = null): Result<List<Tag>>
}
