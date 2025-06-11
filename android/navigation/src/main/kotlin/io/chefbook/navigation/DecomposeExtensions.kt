package io.chefbook.navigation

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.instancekeeper.getOrCreate


val InstanceKeeperOwner.viewModelStoreOwner get() =
  this.instanceKeeper.getOrCreate(::ViewModelStoreOwnerInstance)

class ViewModelStoreOwnerInstance : ViewModelStoreOwner, InstanceKeeper.Instance {
  override val viewModelStore: ViewModelStore = ViewModelStore()

  override fun onDestroy() {
    viewModelStore.clear()
  }
}
