package com.cactusknights.chefbook.ui.screens.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.ui.screens.about.models.AboutScreenEffect
import com.cactusknights.chefbook.ui.screens.about.models.AboutScreenEvent
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AboutScreenViewModel : ViewModel(), EventHandler<AboutScreenEvent> {

    private val _effect: MutableSharedFlow<AboutScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<AboutScreenEffect> = _effect.asSharedFlow()

    override fun obtainEvent(event: AboutScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is AboutScreenEvent.Back -> _effect.emit(AboutScreenEffect.OnBack)
            }
        }
    }

}
