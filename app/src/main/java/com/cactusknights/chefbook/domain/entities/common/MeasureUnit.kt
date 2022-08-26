package com.cactusknights.chefbook.domain.entities.common

sealed class MeasureUnit {
    object G : MeasureUnit()
    object KG : MeasureUnit()
    object ML : MeasureUnit()
    object L : MeasureUnit()
    object PCS : MeasureUnit()
    object TSP : MeasureUnit()
    object TBSP : MeasureUnit()
    class Custom(val name: String) : MeasureUnit()
}
