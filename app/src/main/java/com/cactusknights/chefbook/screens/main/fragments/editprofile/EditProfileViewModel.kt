package com.cactusknights.chefbook.screens.main.fragments.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.domain.usecases.UserUseCases
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.screens.main.fragments.editprofile.models.EditProfileEvent
import com.cactusknights.chefbook.screens.main.fragments.editprofile.models.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel(), EventHandler<EditProfileEvent> {

    var currentUser : User = User()

    private val _profileState: MutableStateFlow<EditProfileState> = MutableStateFlow(
        EditProfileState.Loading)
    val profileState: StateFlow<EditProfileState> = _profileState.asStateFlow()

    private val _messageViewEffect: MutableSharedFlow<Int> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val messageViewEffect: SharedFlow<Int> = _messageViewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            launch { userUseCases.getUserInfo().collect {} }
            userUseCases.listenToUser().collect {
                if (it != null) {
                    currentUser = it
                    _profileState.emit(EditProfileState.ProfileLoaded(it)) }
                }
        }
    }

    override fun obtainEvent(event: EditProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is EditProfileEvent.UploadAvatar -> {
                    if (event.context != null) {
                        val file = File(event.uri)
                        val compressedFile = Compressor.compress(event.context, file) {
                            default()
                            resolution(512, 512)
                            size(1048576)
                        }
                        userUseCases.uploadAvatar(compressedFile.canonicalPath).collect { result ->
                            if (result is Result.Success) {
                                _messageViewEffect.emit(R.string.avatar_updated)
                            }
                        }
                    }
                }
                is EditProfileEvent.DeleteAvatar -> { userUseCases.deleteAvatar().collect { result ->
                    if (result is Result.Success) {
                        _messageViewEffect.emit(R.string.avatar_deleted)
                    }
                } }
                is EditProfileEvent.ChangeName -> {
                    if (event.name.isNotEmpty() && event.name != currentUser.name) {
                        userUseCases.changeName(event.name).collect { result ->
                            if (result is Result.Success) {
                                _messageViewEffect.emit(R.string.name_changed)
                            }
                        }
                    }
                }
            }
        }
    }
}