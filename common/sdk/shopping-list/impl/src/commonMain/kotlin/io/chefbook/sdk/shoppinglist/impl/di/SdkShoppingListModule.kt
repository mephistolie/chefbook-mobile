package io.chefbook.sdk.shoppinglist.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.AddToShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.CreatePurchaseUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListsUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.ObserveShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.RemovePurchasedItemsUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.SwitchPurchaseStatusUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.UpdatePurchaseUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository
import io.chefbook.sdk.shoppinglist.impl.data.repositories.ShoppingListRepositoryImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.LocalShoppingListDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.LocalShoppingListDataSourceImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.PendingUploadsDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.PendingUploadsDataSourceImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.PendingUploadsDataStore
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.PendingUploadsDataStoreImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.ShoppingListsDataStore
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.ShoppingListsDataStoreImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.RemoteShoppingListDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.RemoteShoppingListDataSourceImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListApiService
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListApiServiceImpl
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListUsersApiService
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListUsersApiServiceImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.AddToShoppingListUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.CreatePurchaseUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.GetShoppingListUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.GetShoppingListsUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.ObserveShoppingListUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.RemovePurchasedItemsUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.SwitchPurchaseStatusUseCaseImpl
import io.chefbook.sdk.shoppinglist.impl.domain.usecases.UpdatePurchaseUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkShoppingListModule() = module {

  singleOf(::ShoppingListsDataStoreImpl) bind ShoppingListsDataStore::class
  singleOf(::PendingUploadsDataStoreImpl) bind PendingUploadsDataStore::class

  scope<ProfileComponent> {

    scopedOf(::ShoppingListApiServiceImpl) bind ShoppingListApiService::class

    scopedOf(::ShoppingListUsersApiServiceImpl) bind ShoppingListUsersApiService::class

    scoped<LocalShoppingListDataSource>(named(DataSource.LOCAL)) {
      LocalShoppingListDataSourceImpl(
        profileId = get<ProfileComponent>().profileId,
        dataStore = get(),
      )
    }

    scoped<RemoteShoppingListDataSource>(named(DataSource.REMOTE)) {
      RemoteShoppingListDataSourceImpl(
        api = get(),
      )
    }

    scoped<PendingUploadsDataSource>(named(DataSource.LOCAL)) {
      PendingUploadsDataSourceImpl(
        dataStore = get(),
      )
    }

    scoped<ShoppingListRepository> {
      ShoppingListRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        pendingUploads = get(named(DataSource.LOCAL)),
        sources = get(),
        profileScope = get<ProfileComponent>().coroutineScope,
        dispatchers = get(),
      )
    }

    factoryOf(::GetShoppingListsUseCaseImpl) bind GetShoppingListsUseCase::class
    factoryOf(::ObserveShoppingListUseCaseImpl) bind ObserveShoppingListUseCase::class
    factoryOf(::GetShoppingListUseCaseImpl) bind GetShoppingListUseCase::class
    factoryOf(::RemovePurchasedItemsUseCaseImpl) bind RemovePurchasedItemsUseCase::class
    factoryOf(::CreatePurchaseUseCaseImpl) bind CreatePurchaseUseCase::class
    factoryOf(::UpdatePurchaseUseCaseImpl) bind UpdatePurchaseUseCase::class
    factoryOf(::SwitchPurchaseStatusUseCaseImpl) bind SwitchPurchaseStatusUseCase::class
    factoryOf(::AddToShoppingListUseCaseImpl) bind AddToShoppingListUseCase::class
  }
}
