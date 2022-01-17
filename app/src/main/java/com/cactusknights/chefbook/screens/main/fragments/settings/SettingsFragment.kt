package com.cactusknights.chefbook.screens.main.fragments.settings

import android.content.ComponentName
import android.content.pm.PackageManager
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
import com.cactusknights.chefbook.databinding.FragmentSettingsBinding
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.main.fragments.settings.aliases.OldIconAlias
import com.cactusknights.chefbook.screens.main.fragments.settings.aliases.StandardIconAlias
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsEvent
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SettingsViewModel by viewModels()

    private var settings = Settings()

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
            viewModel.obtainEvent(SettingsEvent.SetShoppingListDefault(!settings.isShoppingListDefault))
        }

        binding.switchLocalSource.setOnClickListener {
            viewModel.obtainEvent(SettingsEvent.SetDataSource(if (settings.dataSource == DataSource.REMOTE) DataSource.LOCAL else DataSource.REMOTE))
        }

        binding.cvTheme.setOnClickListener {
            when (settings.theme) {
                AppTheme.LIGHT -> {
                    viewModel.obtainEvent(SettingsEvent.SetTheme(AppTheme.DARK))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                AppTheme.DARK -> {
                    viewModel.obtainEvent(SettingsEvent.SetTheme(AppTheme.SYSTEM))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                AppTheme.SYSTEM -> {
                    viewModel.obtainEvent(SettingsEvent.SetTheme(AppTheme.LIGHT))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.settingsState.collect { state -> render(state) }
        } }
    }

    private fun render(state: SettingsState) {
        settings = state.settings
        binding.switchShoppingListDefault.isChecked = settings.isShoppingListDefault
        binding.cvLocalSource.visibility = if (settings.userType == UserType.LOCAL) View.GONE else View.VISIBLE
        binding.switchLocalSource.isChecked = settings.dataSource == DataSource.LOCAL
        binding.textIcon.text = when (settings.icon) {
            AppIcon.STANDARD -> resources.getString(R.string.icon_standard)
            AppIcon.OLD -> resources.getString(R.string.icon_old)
        }
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (settings.icon == AppIcon.STANDARD) R.drawable.ic_broccy else R.drawable.ic_logo))

        binding.cvAppIcon.setOnClickListener {
            if (settings.icon == AppIcon.STANDARD) {
                viewModel.obtainEvent(SettingsEvent.SetIcon(AppIcon.OLD, requireActivity()))
            } else {
                viewModel.obtainEvent(SettingsEvent.SetIcon(AppIcon.STANDARD, requireActivity()))
            }
        }

        val isSystemLight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
        when (settings.theme) {
            AppTheme.LIGHT -> customizeThemeButton(true, resources.getString(R.string.theme_light))
            AppTheme.DARK -> customizeThemeButton(false, resources.getString(R.string.theme_dark))
            else -> customizeThemeButton(isSystemLight, resources.getString(R.string.theme_system))
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