package com.cactusknights.chefbook.screens.main.fragments.profile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.FragmentProfileBinding
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileEvent
import com.cactusknights.chefbook.screens.main.fragments.profile.models.ProfileState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ProfileFragmentViewModel by viewModels()
    private val activityViewModel by activityViewModels<NavigationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvSignIn.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenAuthScreen) }
        binding.cvBroccoins.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenBroccoinsDialog) }
        binding.cvSubscription.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenSubscriptionDialog) }
        binding.cvEditProfile.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenEditProfileDialog) }
        binding.cvAppSettings.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenSettingsDialog) }
        binding.cvRate.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.RateApp) }
        binding.cvAboutApp.setOnClickListener { activityViewModel.obtainEvent(NavigationEvent.OpenAboutAppDialog) }
        binding.cvLogout.setOnClickListener { viewModel.obtainEvent(ProfileEvent.SignOut) }

        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.profileState.collect { state -> render(state) }
        } }
    }

    private fun render(state: ProfileState) {
        if (state is ProfileState.ProfileLoaded) {
            binding.llInfo.visibility = if (state.user.isLocal) View.GONE else View.VISIBLE
            binding.cvSignIn.visibility = if (state.user.isLocal) View.VISIBLE else View.GONE
            binding.cvLogout.visibility = if (state.user.isLocal) View.GONE else View.VISIBLE
            binding.cvAdvertisement.visibility = if (state.user.isLocal) View.GONE else View.VISIBLE
            binding.cvBroccoins.visibility = if (state.user.isLocal) View.GONE else View.VISIBLE
            binding.cvEditProfile.visibility = if (state.user.isLocal) View.GONE else View.VISIBLE
            if (state.user.premium != null && (state.user.premium.time - Date().time) > 0) {
                binding.textSubscription.text = resources.getString(R.string.premium_plan)
                binding.ivSubscription.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deep_orange_light))
            } else {
                binding.textSubscription.text = resources.getString(R.string.free_plan)
                binding.ivSubscription.setColorFilter(ContextCompat.getColor(requireContext(), R.color.navigation_secondary))
            }

            binding.textName.text = if (state.user.isLocal) resources.getString(R.string.local_profile) else state.user.name
            binding.textId
            if (!state.user.isLocal) {
                val idStr = "#${state.user.id}"; binding.textId.text = idStr
                binding.textEmail.text = state.user.email
                binding.textBroccoins.text = state.user.broccoins.toString()
            }

            if (!state.user.avatar.isNullOrEmpty()) {
                binding.ivAvatar.load(state.user.avatar) { this.crossfade(true) }
                binding.ivAvatar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            } else {
                val dp = resources.displayMetrics.density
                binding.ivAvatar.load(R.drawable.ic_user)
                binding.ivAvatar.layoutParams = FrameLayout.LayoutParams((64*dp).toInt(), (64*dp).toInt()).apply { gravity = Gravity.CENTER }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}