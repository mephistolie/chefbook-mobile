package io.chefbook.libs.mvi.decompose

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.instancekeeper.retainedInstance
import io.chefbook.libs.mvi.Store
import org.koin.core.component.KoinComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.parameter.ParametersDefinition

inline fun <reified T : Store<*, *, *>> InstanceKeeperOwner.retainedStore(
  scope: KoinScopeComponent? = null,
  noinline parameters: ParametersDefinition? = null,
): Lazy<T> {
  val store = retainedInstance {
    object : InstanceKeeper.Instance, KoinComponent {

      val instance = (scope ?: this).inject<T>(parameters = parameters)

      override fun onDestroy() {
        if (instance.isInitialized()){
          instance.value.dispose()
        }
      }
    }
  }

  return store.instance
}
