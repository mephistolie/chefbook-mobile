package com.cactusknights.chefbook.screens.main.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.domain.usecases.ProfileUseCases
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: ProfileUseCases
) : ViewModel(), EventHandler<ProfileScreenEvent> {

    private val _profileState: MutableStateFlow<ProfileScreenState> = MutableStateFlow(
        ProfileScreenState.Loading)
    val profileState: StateFlow<ProfileScreenState> = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            launch { userUseCases.getUserInfo().collect {} }
            launch {
                userUseCases.listenToUser().collect { if (it != null)
                    _profileState.emit(ProfileScreenState.ProfileLoaded(it))
                }
            }
        }
    }

    override fun obtainEvent(event: ProfileScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is ProfileScreenEvent.SignOut -> authUseCases.signOut().collect {}
            }
        }
    }
}