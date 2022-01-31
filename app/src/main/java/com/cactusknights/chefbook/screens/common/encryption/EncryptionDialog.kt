package com.cactusknights.chefbook.screens.common.encryption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.databinding.DialogEncryptionBinding
import com.cactusknights.chefbook.screens.common.ConfirmDialog
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenEvent
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenState
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenViewEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EncryptionDialog: DialogFragment() {

    private val viewModel by activityViewModels<EncryptionViewModel>()

    private var _binding: DialogEncryptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEncryptionBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.layoutCreation.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.layoutCreation.btnConfirm.setOnClickListener {
            viewModel.obtainEvent(EncryptionScreenEvent.CreateVault(binding.layoutCreation.inputPassword.text.toString(), binding.layoutCreation.inputPasswordVerification.text.toString()))
            binding.layoutCreation.inputPassword.setText("")
            binding.layoutCreation.inputPasswordVerification.setText("")
        }

        binding.layoutUnlocking.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.layoutUnlocking.btnConfirm.setOnClickListener {
            viewModel.obtainEvent(EncryptionScreenEvent.UnlockVault(binding.layoutUnlocking.inputPassword.text.toString()))
            binding.layoutUnlocking.inputPassword.setText("")
        }

        binding.layoutManage.textLockVault.setOnClickListener {
            viewModel.obtainEvent(EncryptionScreenEvent.LockVault)
        }

        binding.layoutManage.textDeleteRecipe.setOnClickListener {
            ConfirmDialog { viewModel.obtainEvent(EncryptionScreenEvent.DeleteVault) }.show(requireActivity().supportFragmentManager, "Confirm")
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.encryptionState.collect { render(it) } }
                launch { viewModel.viewEffect.collect { handleViewEffect(it) } }
            }
        }
    }

    private fun render(state: EncryptionScreenState) {
        when (state) {
            is EncryptionScreenState.Disabled -> {
                binding.layoutCreation.root.visibility = View.VISIBLE
                binding.layoutUnlocking.root.visibility = View.GONE
                binding.layoutManage.root.visibility = View.GONE
                binding.progress.visibility = View.GONE
            }
            is EncryptionScreenState.Locked -> {
                binding.layoutCreation.root.visibility = View.GONE
                binding.layoutUnlocking.root.visibility = View.VISIBLE
                binding.layoutManage.root.visibility = View.GONE
                binding.progress.visibility = View.GONE
            }
            is EncryptionScreenState.Unlocked -> {
                binding.layoutCreation.root.visibility = View.GONE
                binding.layoutUnlocking.root.visibility = View.GONE
                binding.layoutManage.root.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
            }
            EncryptionScreenState.Unlocking -> {
                binding.layoutCreation.root.visibility = View.GONE
                binding.layoutUnlocking.root.visibility = View.GONE
                binding.layoutManage.root.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
            }
        }
    }

    private fun handleViewEffect(event: EncryptionScreenViewEffect) {
        when (event) {
            is EncryptionScreenViewEffect.Done -> {
                dialog?.dismiss()
            }
            is EncryptionScreenViewEffect.Message -> requireContext().showToast(event.messageId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}