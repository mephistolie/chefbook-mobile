package com.cactusknights.chefbook.di

import android.content.Context
import androidx.room.Room
import com.cactusknights.chefbook.common.ChefBookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ChefBookDatabase::class.java,
            "chefbook_database"
        ).build()
}