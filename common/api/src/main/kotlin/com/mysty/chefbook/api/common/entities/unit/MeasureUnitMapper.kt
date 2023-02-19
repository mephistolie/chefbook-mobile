package com.mysty.chefbook.api.common.entities.unit

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

}
