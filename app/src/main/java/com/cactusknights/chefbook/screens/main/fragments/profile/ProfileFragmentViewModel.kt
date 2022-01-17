package com.cactusknights.chefbook.screens.main.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.domain.usecases.UserUseCases
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileEvent
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases
) : ViewModel(), EventHandler<ProfileEvent> {

    private val _profileState: MutableStateFlow<ProfileState> = MutableStateFlow(
        ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            launch { userUseCases.getUserInfo().collect {} }
            userUseCases.listenToUser().collect { if (it != null) _profileState.emit(
                ProfileState.ProfileLoaded(it)) }
        }
    }

    override fun obtainEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is ProfileEvent.SignOut -> authUseCases.signOut().collect {}
            }
        }
    }
}