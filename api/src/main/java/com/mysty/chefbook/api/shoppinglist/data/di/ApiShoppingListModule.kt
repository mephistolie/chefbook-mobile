package com.mysty.chefbook.api.shoppinglist.data.di

import com.mysty.chefbook.api.recipe.data.local.DataStoreUtils
import com.mysty.chefbook.api.shoppinglist.data.local.LocalShoppingListSource
import com.mysty.chefbook.api.shoppinglist.data.remote.RemoteShoppingListSource
import com.mysty.chefbook.api.shoppinglist.data.remote.api.ShoppingListApi
import com.mysty.chefbook.api.shoppinglist.data.repository.IShoppingListSource
import com.mysty.chefbook.api.shoppinglist.data.repository.ShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.usecases.AddToShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.GetShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IAddToShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IGetShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IObserveShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IRemovePurchasedItemsUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISetShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISwitchPurchaseStatusUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISyncShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ObserveShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.RemovePurchasedItemsUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.SetShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.SwitchPurchaseStatusUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.SyncShoppingListUseCase
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val apiShoppingListModule = module {

    singleOf(DataStoreUtils::getShoppingListDataStore) { named(Qualifiers.DataStore.SHOPPING_LIST) }

    single { createShoppingListApi(get(named(Qualifiers.AUTHORIZED))) }

    single(named(Qualifiers.LOCAL)) { LocalShoppingListSource(get(named(Qualifiers.DataStore.SHOPPING_LIST))) } bind IShoppingListSource::class
    singleOf(::RemoteShoppingListSource) { named(Qualifiers.REMOTE) } bind IShoppingListSource::class

    single<IShoppingListRepo> { ShoppingListRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }

    factoryOf(::ObserveShoppingListUseCase) bind IObserveShoppingListUseCase::class
    factoryOf(::GetShoppingListUseCase) bind IGetShoppingListUseCase::class
    factoryOf(::SyncShoppingListUseCase) bind ISyncShoppingListUseCase::class
    factoryOf(::RemovePurchasedItemsUseCase) bind IRemovePurchasedItemsUseCase::class
    factoryOf(::SwitchPurchaseStatusUseCase) bind ISwitchPurchaseStatusUseCase::class
    factoryOf(::SetShoppingListUseCase) bind ISetShoppingListUseCase::class
    factoryOf(::AddToShoppingListUseCase) bind IAddToShoppingListUseCase::class

}

private fun createShoppingListApi(retrofit: Retrofit) = retrofit.create(ShoppingListApi::class.java)
