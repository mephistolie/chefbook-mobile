package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.cache.CategoriesCache
import com.cactusknights.chefbook.data.cache.RecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCache
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCacheReader
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCacheWriter
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCacheReader
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCacheWriter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CacheModule {

    @Binds
    fun bindCategoriesCache(cache: CategoriesCache): ICategoriesCache

    @Binds
    fun bindCategoriesCacheReader(cache: ICategoriesCache): ICategoriesCacheReader

    @Binds
    fun bindCategoriesCacheWriter(cache: ICategoriesCache): ICategoriesCacheWriter

    @Binds
    fun bindRecipeBookCache(cache: RecipeBookCache): IRecipeBookCache

    @Binds
    fun bindRecipeBookCacheReader(cache: IRecipeBookCache): IRecipeBookCacheReader

    @Binds
    fun bindRecipeBookCacheWriter(cache: IRecipeBookCache): IRecipeBookCacheWriter

}
