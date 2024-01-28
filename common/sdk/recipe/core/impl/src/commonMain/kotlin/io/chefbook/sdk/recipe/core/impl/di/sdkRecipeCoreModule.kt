package io.chefbook.sdk.recipe.core.impl.di

import io.chefbook.sdk.recipe.core.impl.data.cache.RecipesCacheImpl
import io.chefbook.sdk.recipe.book.api.internal.data.cache.RecipeBookCache
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCacheReader
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCacheWriter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val sdkRecipeCoreModule = module {

  singleOf(::RecipesCacheImpl) binds arrayOf(
    RecipeBookCache::class,
    RecipesCache::class,
    RecipesCacheReader::class,
    RecipesCacheWriter::class
  )
}
