package com.cactusknights.chefbook

import android.content.SharedPreferences
import com.cactusknights.chefbook.models.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class DataSources {

    LOCAL, REMOTE;

    companion object {
        fun getDataSourceByString(input: String) : DataSources {
            return if (input.lowercase() == "remote") REMOTE else LOCAL
        }
    }
}

class Synchronizer @Inject constructor(val sp: SharedPreferences) {

    var dataSource : DataSources = DataSources.LOCAL
    val user : StateFlow<User> = null

    init {
        dataSource = DataSources.getDataSourceByString(sp.getString("", DataSources.LOCAL.toString()).orEmpty())
    }


    fun getCurrentUser() {
        if (dataSource == DataSources.LOCAL)
    }
}