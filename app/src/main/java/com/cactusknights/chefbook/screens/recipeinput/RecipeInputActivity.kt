package com.cactusknights.chefbook.screens.recipeinput

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.databinding.ActivityRecipeInputBinding
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.common.ConfirmDialog
import com.cactusknights.chefbook.screens.common.encryption.EncryptionDialog
import com.cactusknights.chefbook.screens.common.encryption.EncryptionViewModel
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenState
import com.cactusknights.chefbook.screens.common.loading.LoadingDialog
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.recipe.dialogs.RecipeDialog
import com.cactusknights.chefbook.screens.recipeinput.adapters.RecipeInputViewPagerAdapter
import com.cactusknights.chefbook.screens.recipeinput.dialogs.RecipeEncryptionDialog
import com.cactusknights.chefbook.screens.recipeinput.dialogs.VisibilityDialog
import com.cactusknights.chefbook.screens.recipeinput.models.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RecipeInputActivity: AppCompatActivity() {

    private val recipeInputViewModel: RecipeInputViewModel by viewModels()
    private val encryptionViewModel : EncryptionViewModel by viewModels()
    private lateinit var binding: ActivityRecipeInputBinding

    private val vaultEncryptionDialog = EncryptionDialog()
    private val recipeEncryptionMenu = RecipeEncryptionDialog()
    private val visibilityMenu = VisibilityDialog()
    private val loadingAlert = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeInputViewModel.obtainEvent(RecipeInputScreenEvent.CreateRecipe)
        if (savedInstanceState == null) {
            val originalRecipe = intent.extras?.get("recipe") as DecryptedRecipe?
            if (originalRecipe != null) recipeInputViewModel.obtainEvent(RecipeInputScreenEvent.EditRecipe(originalRecipe))
        }

        binding.vpRecipeInput.adapter = RecipeInputViewPagerAdapter(supportFragmentManager, this.lifecycle)
        binding.vpRecipeInput.isUserInputEnabled = false
        binding.btnBack.setOnClickListener {
            when (binding.vpRecipeInput.currentItem) {
                0 -> onBackPressed()
                else -> {
                    binding.vpRecipeInput.currentItem = binding.vpRecipeInput.currentItem-1
                    binding.btnConfirm.text = resources.getString(R.string.continue_text)
                }
            }

        }

        binding.btnVisibility.setOnClickListener {
            visibilityMenu.show(supportFragmentManager, "Visibility Menu")
        }

        binding.btnEncryption.setOnClickListener {
            if (encryptionViewModel.encryptionState.value != EncryptionScreenState.Unlocked) {
                vaultEncryptionDialog.show(supportFragmentManager, MainActivity.ENCRYPTION_DIALOG)
            } else {
                recipeEncryptionMenu.show(supportFragmentManager, MainActivity.ENCRYPTION_DIALOG)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                encryptionViewModel.listenToEncryption()
                launch { recipeInputViewModel.recipeInputState.collect { render(it) } }
                launch { recipeInputViewModel.viewEffect.collect { handleViewEffect(it) } }
            }
        }
    }

    private fun render(state: RecipeInputScreenState) {
        if (state is RecipeInputScreenState.Loading) loadingAlert.show(supportFragmentManager, "Loading")
        else loadingAlert.dialog?.cancel()

        var recipe : DecryptedRecipe? = null
        if (state is RecipeInputScreenState.NewRecipe) recipe = state.recipeInput
        if (state is RecipeInputScreenState.EditRecipe) {
            recipe = state.recipeInput
            binding.textSection.setText(R.string.edit_recipe)
            binding.btnEncryption.isEnabled = false
        }
        if (recipe != null) {
            binding.btnEncryption.setImageDrawable(ContextCompat.getDrawable(this, if (recipe.isEncrypted) R.drawable.ic_lock else R.drawable.ic_lock_open))
            binding.btnVisibility.setImageDrawable(ContextCompat.getDrawable(this,
                when(recipe.visibility) {
                    Visibility.PRIVATE -> R.drawable.ic_eye_closed
                    Visibility.SHARED -> R.drawable.ic_link
                    Visibility.PUBLIC -> R.drawable.ic_online
                }
            ))
            binding.btnConfirm.setOnClickListener {
                when (binding.vpRecipeInput.currentItem) {
                    1 -> recipeInputViewModel.obtainEvent(RecipeInputScreenEvent.ConfirmInput)
                    else -> {
                        if (recipe.name.isEmpty()) {
                            showToast(R.string.enter_name)
                        } else {
                            binding.vpRecipeInput.currentItem = binding.vpRecipeInput.currentItem+1
                            binding.btnConfirm.text = resources.getString(R.string.confirm_text)
                        }
                    }
                }
            }
        }
    }

    private fun handleViewEffect(effect: RecipeInputScreenViewEffect) {
        when (effect) {
            is RecipeInputScreenViewEffect.Message -> this@RecipeInputActivity.showToast(effect.messageId)
            is RecipeInputScreenViewEffect.InputConfirmed -> {
                setResult(RESULT_OK, Intent().putExtra("recipe", effect.committedRecipe))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        ConfirmDialog { super.onBackPressed() }.show(supportFragmentManager, "Confirm")
    }
}