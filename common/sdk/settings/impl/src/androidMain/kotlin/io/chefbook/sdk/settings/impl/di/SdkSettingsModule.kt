package io.chefbook.sdk.settings.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.GetDefaultRecipeLanguageUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.GetSettingsUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppIconUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppThemeUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetDefaultRecipeLanguageUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetEnvironmentUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetOpenSavedRecipeExpandedUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.ProfileModeRepository
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcherImpl
import io.chefbook.sdk.settings.impl.data.repositories.ProfileModeRepositoryImpl
import io.chefbook.sdk.settings.impl.data.repositories.SettingsRepositoryImpl
import io.chefbook.sdk.settings.impl.data.sources.SettingsDataSource
import io.chefbook.sdk.settings.impl.data.sources.local.LocalSettingsDataSourceImpl
import io.chefbook.sdk.settings.impl.domain.usecases.ObserveCommunityRecipesLanguagesUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.GetDefaultRecipeLanguageUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.GetSettingsUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.ObserveSettingsUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetAppIconUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetAppThemeUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetCommunityRecipesLanguagesUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetDefaultRecipeLanguageUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetEnvironmentUseCaseImpl
import io.chefbook.sdk.settings.impl.domain.usecases.SetOpenSavedRecipeExpandedUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkSettingsModule = module {

  singleOf(::IconSwitcherImpl) bind IconSwitcher::class

  single<SettingsDataSource>(named(DataSource.LOCAL)) { LocalSettingsDataSourceImpl(get()) }

  single<SettingsRepository> { SettingsRepositoryImpl(get(named(DataSource.LOCAL))) }
  singleOf(::ProfileModeRepositoryImpl) bind ProfileModeRepository::class

  factoryOf(::ObserveSettingsUseCaseImpl) bind ObserveSettingsUseCase::class
  factoryOf(::GetSettingsUseCaseImpl) bind GetSettingsUseCase::class
  factoryOf(::GetDefaultRecipeLanguageUseCaseImpl) bind GetDefaultRecipeLanguageUseCase::class
  factoryOf(::SetDefaultRecipeLanguageUseCaseImpl) bind SetDefaultRecipeLanguageUseCase::class
  factoryOf(::SetAppThemeUseCaseImpl) bind SetAppThemeUseCase::class
  factoryOf(::SetAppIconUseCaseImpl) bind SetAppIconUseCase::class
  factoryOf(::SetDefaultRecipeLanguageUseCaseImpl) bind SetDefaultRecipeLanguageUseCase::class
  factoryOf(::SetOpenSavedRecipeExpandedUseCaseImpl) bind SetOpenSavedRecipeExpandedUseCase::class
  factoryOf(::SetEnvironmentUseCaseImpl) bind SetEnvironmentUseCase::class
  factoryOf(::ObserveCommunityRecipesLanguagesUseCaseImpl) bind ObserveCommunityRecipesLanguagesUseCase::class
  factoryOf(::SetCommunityRecipesLanguagesUseCaseImpl) bind SetCommunityRecipesLanguagesUseCase::class
}
