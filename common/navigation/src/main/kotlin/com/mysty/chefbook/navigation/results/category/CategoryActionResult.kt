package com.mysty.chefbook.navigation.results.category

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CategoryActionResult : Parcelable {
    data class Created(val id: String, val name: String, val cover: String?) : CategoryActionResult()
    data class Updated(val id: String, val name: String, val cover: String?) : CategoryActionResult()
    data class Deleted(val categoryId: String) : CategoryActionResult()
}
