package com.cactusknights.chefbook.screens.main.fragments.settings

import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.databinding.FragmentSettingsBinding
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SettingsViewModel by viewModels()

    private var settings = SettingsProto.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchShoppingListDefault.setOnClickListener {
            viewModel.obtainEvent(SettingsScreenEvent.SetDefaultTab(if (settings.defaultTab == SettingsProto.Tabs.RECIPES) SettingsProto.Tabs.SHOPPING_LIST else SettingsProto.Tabs.RECIPES))
        }

        binding.switchLocalSource.setOnClickListener {
            viewModel.obtainEvent(SettingsScreenEvent.SetDataSource(if (settings.dataSource == SettingsProto.DataSource.REMOTE) SettingsProto.DataSource.LOCAL else SettingsProto.DataSource.REMOTE))
        }

        binding.cvTheme.setOnClickListener {
            when (settings.appTheme) {
                SettingsProto.AppTheme.LIGHT -> {
                    viewModel.obtainEvent(SettingsScreenEvent.SetTheme(SettingsProto.AppTheme.DARK))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                SettingsProto.AppTheme.DARK -> {
                    viewModel.obtainEvent(SettingsScreenEvent.SetTheme(SettingsProto.AppTheme.SYSTEM))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                else -> {
                    viewModel.obtainEvent(SettingsScreenEvent.SetTheme(SettingsProto.AppTheme.LIGHT))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.settingsState.collect { state -> render(state) }
        } }
    }

    private fun render(state: SettingsScreenState) {
        if (state is SettingsScreenState.SettingsLoaded) {
            settings = state.settings
            binding.switchShoppingListDefault.isChecked = settings.defaultTab == SettingsProto.Tabs.SHOPPING_LIST
            binding.cvLocalSource.visibility = if (settings.userType == SettingsProto.UserType.OFFLINE) View.GONE else View.VISIBLE
            binding.switchLocalSource.isChecked = settings.dataSource == SettingsProto.DataSource.LOCAL
            binding.textIcon.text = when (settings.appIcon) {
                SettingsProto.AppIcon.OLD-> resources.getString(R.string.icon_old)
                else -> resources.getString(R.string.icon_standard)
            }
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (settings.appIcon == SettingsProto.AppIcon.STANDARD) R.drawable.ic_broccy else R.drawable.ic_logo))

            binding.cvAppIcon.setOnClickListener {
                if (settings.appIcon == SettingsProto.AppIcon.STANDARD) {
                    viewModel.obtainEvent(SettingsScreenEvent.SetIcon(SettingsProto.AppIcon.OLD, requireActivity()))
                } else {
                    viewModel.obtainEvent(SettingsScreenEvent.SetIcon(SettingsProto.AppIcon.STANDARD, requireActivity()))
                }
            }

            val isSystemLight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
            when (settings.appTheme) {
                SettingsProto.AppTheme.LIGHT -> customizeThemeButton(true, resources.getString(R.string.theme_light))
                SettingsProto.AppTheme.DARK -> customizeThemeButton(false, resources.getString(R.string.theme_dark))
                else -> customizeThemeButton(isSystemLight, resources.getString(R.string.theme_system))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun customizeThemeButton(isLight: Boolean, modeName: String) {
        if (isLight) {
            binding.cvThemeBackground.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_light_background))
            binding.ivTheme.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_light))
            binding.ivTheme.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_light_theme))
        } else {
            binding.cvThemeBackground.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.deep_purple_light_background))
            binding.ivTheme.setColorFilter(ContextCompat.getColor(requireContext(), R.color.deep_purple_light))
            binding.ivTheme.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_theme))
        }
        binding.textTheme.text = modeName
    }
}