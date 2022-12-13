package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.cache.CategoriesCache
import com.cactusknights.chefbook.data.cache.RecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCache
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCacheReader
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCacheWriter
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCacheReader
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCacheWriter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val cacheModule = module {
    singleOf(::CategoriesCache) binds arrayOf(ICategoriesCache::class, ICategoriesCacheReader::class, ICategoriesCacheWriter::class)
    singleOf(::RecipeBookCache) binds arrayOf(IRecipeBookCache::class, IRecipeBookCacheReader::class, IRecipeBookCacheWriter::class)
}
