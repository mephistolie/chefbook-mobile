package io.chefbook.sdk.recipe.core.impl.di

import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.recipe.book.api.internal.data.cache.RecipeBookCache
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCacheReader
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCacheWriter
import io.chefbook.sdk.recipe.core.impl.data.cache.RecipesCacheImpl
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.binds
import org.koin.dsl.module

fun sdkRecipeCoreModule() = module {

  scope<ProfileComponent> {
    scopedOf(::RecipesCacheImpl) binds arrayOf(
      RecipeBookCache::class,
      RecipesCache::class,
      RecipesCacheReader::class,
      RecipesCacheWriter::class
    )
  }
}
