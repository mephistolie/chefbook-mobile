package com.mysty.chefbook.api.settings.data.di

import com.mysty.chefbook.api.recipe.data.local.DataStoreUtils
import com.mysty.chefbook.api.settings.data.SettingsRepo
import com.mysty.chefbook.api.settings.domain.ISettingsRepo
import com.mysty.chefbook.api.settings.domain.usecases.IObserveSettingsUseCase
import com.mysty.chefbook.api.settings.domain.usecases.ISetDefaultRecipeLanguageUseCase
import com.mysty.chefbook.api.settings.domain.usecases.ObserveSettingsUseCase
import com.mysty.chefbook.api.settings.domain.usecases.SetDefaultRecipeLanguageUseCase
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val apiSettingsModule = module {

    singleOf(DataStoreUtils::getSettingsDataStore) { named(Qualifiers.DataStore.SETTINGS) }

    single<ISettingsRepo> { SettingsRepo(get(named(Qualifiers.DataStore.SETTINGS))) }

    factoryOf(::ObserveSettingsUseCase) bind IObserveSettingsUseCase::class
    factoryOf(::SetDefaultRecipeLanguageUseCase) bind ISetDefaultRecipeLanguageUseCase::class

}
