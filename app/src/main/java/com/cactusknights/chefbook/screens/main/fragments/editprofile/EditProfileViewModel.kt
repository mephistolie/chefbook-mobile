package com.cactusknights.chefbook.screens.main.fragments.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.ProfileUseCases
import com.cactusknights.chefbook.models.Profile
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
    private val profileUseCases: ProfileUseCases
) : ViewModel(), EventHandler<EditProfileScreenEvent> {

    var profile : Profile = Profile()

    private val _profileState: MutableStateFlow<EditProfileScreenState> = MutableStateFlow(
        EditProfileScreenState.Loading)
    val profileState: StateFlow<EditProfileScreenState> = _profileState.asStateFlow()

    private val _messageViewEffect: MutableSharedFlow<Int> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val messageViewEffect: SharedFlow<Int> = _messageViewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            launch { profileUseCases.getUserInfo().collect {} }
            profileUseCases.listenToUser().collect {
                if (it != null) {
                    profile = it
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
                        profileUseCases.uploadAvatar(compressedFile.canonicalPath).collect { result ->
                            if (result is Result.Success) {
                                _messageViewEffect.emit(R.string.avatar_updated)
                            }
                        }
                    }
                }
                is EditProfileScreenEvent.DeleteAvatar -> { profileUseCases.deleteAvatar().collect { result ->
                    if (result is Result.Success) {
                        _messageViewEffect.emit(R.string.avatar_deleted)
                    }
                } }
                is EditProfileScreenEvent.ChangeName -> {
                    if (event.name.isNotEmpty() && event.name != profile.username) {
                        profileUseCases.changeName(event.name).collect { result ->
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