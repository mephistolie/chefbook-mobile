package com.cactusknights

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ChefBookApp @Inject constructor(): Application()