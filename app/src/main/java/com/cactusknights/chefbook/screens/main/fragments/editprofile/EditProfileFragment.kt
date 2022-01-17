package com.cactusknights.chefbook.screens.main.fragments.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.common.ConfirmDialog
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.databinding.FragmentEditProfileBinding
import com.cactusknights.chefbook.screens.main.fragments.editprofile.dialogs.ChangeNameDialog
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.main.fragments.editprofile.models.EditProfileEvent
import com.canhub.cropper.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class EditProfileFragment: Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : EditProfileViewModel by viewModels()
    private val activityViewModel by activityViewModels<NavigationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cropImage = registerForActivityResult(CropImageContract()) {
            val uri = it.getUriFilePath(requireContext())
            if (it.isSuccessful && uri != null) {
                viewModel.obtainEvent(EditProfileEvent.UploadAvatar(uri, context))
            }
        }

        binding.cvEditAvatar.setOnClickListener {
            cropImage.launch(
                options {
                    setAspectRatio(1, 1)
                    setCropShape(CropImageView.CropShape.OVAL)
                }
            )
        }

        binding.cvDeleteAvatar.setOnClickListener {
            val fm = activity?.supportFragmentManager
            if (fm != null) ConfirmDialog { viewModel.obtainEvent(EditProfileEvent.DeleteAvatar) }.show(fm, "Confirm")
        }

        binding.cvChangeName.setOnClickListener {
            val fm = activity?.supportFragmentManager
            if (fm != null) ChangeNameDialog(viewModel.currentUser.name) { name -> viewModel.obtainEvent(EditProfileEvent.ChangeName(name)) }.show(fm, "Confirm")
        }

        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.messageViewEffect.collect { state -> render(state) }
        } }
    }

    private fun render(messageId: Int) {
        activity?.showToast(messageId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}