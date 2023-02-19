package com.mysty.chefbook.api.common.entities.unit

sealed class MeasureUnit {
  object G : MeasureUnit()
  object KG : MeasureUnit()
  object ML : MeasureUnit()
  object L : MeasureUnit()
  object PCS : MeasureUnit()
  object TSP : MeasureUnit()
  object TBSP : MeasureUnit()
  class Custom(val name: String) : MeasureUnit()

  companion object {

  }
}

val standardUnits = listOf(
  MeasureUnit.G, MeasureUnit.KG,
  MeasureUnit.ML, MeasureUnit.L,
  MeasureUnit.TSP, MeasureUnit.TBSP,
  MeasureUnit.PCS
)