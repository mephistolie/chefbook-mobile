package com.cactusknights.chefbook.core.mappers

import android.content.res.Resources
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.common.MeasureUnit

object MeasureUnitMapper {
    private val unitsMap = mapOf(
        "g" to MeasureUnit.G,
        "kg" to MeasureUnit.KG,
        "ml" to MeasureUnit.ML,
        "l" to MeasureUnit.L,
        "pcs" to MeasureUnit.PCS,
        "tsp" to MeasureUnit.TSP,
        "tbsp" to MeasureUnit.TBSP,
    )

    private val reversedUnitsMap = unitsMap.entries.associateBy({ it.value }) { it.key }

    fun map(unit: String?) = if (unit != null) unitsMap[unit.lowercase()] ?: MeasureUnit.Custom(unit) else null

    fun map(unit: MeasureUnit?) = when (unit) {
        is MeasureUnit.Custom -> unit.name
        else -> reversedUnitsMap[unit]
    }

    fun map(unit: String?, resources: Resources): MeasureUnit? {
        if (unit.isNullOrBlank()) return null
        val localizedUnitsMap = mapOf(
            resources.getString(R.string.common_general_g).lowercase() to MeasureUnit.G,
            resources.getString(R.string.common_general_kg).lowercase() to MeasureUnit.KG,
            resources.getString(R.string.common_general_ml).lowercase() to MeasureUnit.ML,
            resources.getString(R.string.common_general_l).lowercase() to MeasureUnit.L,
            resources.getString(R.string.common_general_pcs).lowercase() to MeasureUnit.PCS,
            resources.getString(R.string.common_general_tsp).lowercase() to MeasureUnit.TSP,
            resources.getString(R.string.common_general_tbsp).lowercase() to MeasureUnit.TBSP,
        )
        return localizedUnitsMap[unit.lowercase()] ?: MeasureUnit.Custom(unit)
    }

}
