package com.cactusknights.chefbook.screens.main.fragments.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.UserUseCases
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.screens.main.fragments.editprofile.models.EditProfileScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.editprofile.models.EditProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel(), EventHandler<EditProfileScreenEvent> {

    var currentUser : User = User()

    private val _profileState: MutableStateFlow<EditProfileScreenState> = MutableStateFlow(
        EditProfileScreenState.Loading)
    val profileState: StateFlow<EditProfileScreenState> = _profileState.asStateFlow()

    private val _messageViewEffect: MutableSharedFlow<Int> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val messageViewEffect: SharedFlow<Int> = _messageViewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            launch { userUseCases.getUserInfo().collect {} }
            userUseCases.listenToUser().collect {
                if (it != null) {
                    currentUser = it
                    _profileState.emit(EditProfileScreenState.ProfileLoaded(it)) }
                }
        }
    }

    override fun obtainEvent(event: EditProfileScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is EditProfileScreenEvent.UploadAvatar -> {
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
                is EditProfileScreenEvent.DeleteAvatar -> { userUseCases.deleteAvatar().collect { result ->
                    if (result is Result.Success) {
                        _messageViewEffect.emit(R.string.avatar_deleted)
                    }
                } }
                is EditProfileScreenEvent.ChangeName -> {
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