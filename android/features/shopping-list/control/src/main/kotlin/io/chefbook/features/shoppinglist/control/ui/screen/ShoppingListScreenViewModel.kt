package io.chefbook.features.shoppinglist.control.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import io.chefbook.features.shoppinglist.control.R
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ModalState
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenEffect
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenIntent
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenState
import io.chefbook.features.shoppinglist.control.ui.screen.state.ShoppingListSectionMapper
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.CreatePurchaseUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListsUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.ObserveShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.RemovePurchasedItemsUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.SwitchPurchaseStatusUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
internal class ShoppingListScreenViewModel(
  private val getShoppingListsUseCase: GetShoppingListsUseCase,
  private val observeShoppingListUseCase: ObserveShoppingListUseCase,
  private val switchPurchaseStatusUseCase: SwitchPurchaseStatusUseCase,
  private val createPurchaseUseCase: CreatePurchaseUseCase,
  private val removePurchasedItemsUseCase: RemovePurchasedItemsUseCase,

  private val context: Context,
) :
  BaseMviViewModel<ShoppingListScreenState, ShoppingListScreenIntent, ShoppingListScreenEffect>() {

  private var selectedShoppingListId: String? = null
    set(value) {
      field = value
      observeCurrentShoppingList()
    }
  private var observeShoppingListJob: Job? = null

  override val _state: MutableStateFlow<ShoppingListScreenState> =
    MutableStateFlow(ShoppingListScreenState.Loading())

  init {
    observePersonalShoppingList()
  }

  private fun observePersonalShoppingList() {
    viewModelScope.launch {
      getShoppingListsUseCase().onSuccess { shoppingLists ->
        val personalShoppingList =
          shoppingLists.firstOrNull { it.type == ShoppingListMeta.Type.PERSONAL }
        if (personalShoppingList != null) {
          _state.update {
            ShoppingListScreenState.Loading(title = getShoppingListName(personalShoppingList))
          }
        }
        selectedShoppingListId = personalShoppingList?.id
      }
    }
  }

  private fun observeCurrentShoppingList() {
    val shoppingListId = selectedShoppingListId ?: return

    observeShoppingListJob?.cancel()
    observeShoppingListJob = observeShoppingListUseCase(shoppingListId)
      .collectState { state, shoppingList ->
        when {
          shoppingList != null -> ShoppingListScreenState.Loaded(
            title = getShoppingListName(shoppingList.meta),
            sections = ShoppingListSectionMapper.map(shoppingList),
            isDoneButtonEnabled = shoppingList.purchases.any { it.isPurchased },
            modalState = (state as? ShoppingListScreenState.Loaded)?.modalState ?: ModalState.None,
          )

          else -> ShoppingListScreenState.Loading(state.title)
        }
      }
  }

  override suspend fun reduceIntent(intent: ShoppingListScreenIntent) {
    when (intent) {
      is ShoppingListScreenIntent.CreatePurchase -> {
        val shoppingListId = selectedShoppingListId ?: return
        createPurchaseUseCase(shoppingListId)
          .onSuccess { purchaseId ->
            reduceIntent(ShoppingListScreenIntent.OpenPurchaseInput(purchaseId))
          }
      }

      is ShoppingListScreenIntent.OpenPurchaseInput -> {
        _state.update { state ->
          val shoppingListId = selectedShoppingListId ?: return@update state
          if (state is ShoppingListScreenState.Loaded) {
            state.copy(
              modalState = ModalState.PurchaseInput(
                shoppingListId = shoppingListId,
                purchaseId = intent.purchaseId,
              )
            )
          } else {
            state
          }
        }
        _effect.emit(ShoppingListScreenEffect.ModalSheetOpened)
      }

      is ShoppingListScreenIntent.SwitchPurchasedStatus ->
        switchPurchaseStatusUseCase(
          selectedShoppingListId.orEmpty(),
          intent.purchasedId
        )

      is ShoppingListScreenIntent.RemovePurchasedItems ->
        removePurchasedItemsUseCase(selectedShoppingListId.orEmpty())

      is ShoppingListScreenIntent.OpenRecipe ->
        _effect.emit(ShoppingListScreenEffect.OnRecipeOpened(intent.recipeId))

      is ShoppingListScreenIntent.Close -> _effect.emit(ShoppingListScreenEffect.Closed)
    }
  }

  private fun getShoppingListName(shoppingList: ShoppingListMeta): String {
    shoppingList.name?.let { return it }
    return when (shoppingList.type) {
      ShoppingListMeta.Type.PERSONAL -> context.getString(R.string.common_shopping_list_screen_personal)
      ShoppingListMeta.Type.SHARED ->
        "${shoppingList.owner.name ?: ""} #${shoppingList.id.substringBefore('-')}".trim()
    }
  }
}
