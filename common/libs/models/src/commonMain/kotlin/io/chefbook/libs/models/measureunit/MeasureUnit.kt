package io.chefbook.libs.models.measureunit

sealed class MeasureUnit {

  data object G : MeasureUnit()

  data object KG : MeasureUnit()

  data object ML : MeasureUnit()

  data object L : MeasureUnit()

  data object PCS : MeasureUnit()

  data object TSP : MeasureUnit()

  data object TBSP : MeasureUnit()

  data class Custom(val name: String) : MeasureUnit()
}

val standardUnits = listOf(
  MeasureUnit.G, MeasureUnit.KG,
  MeasureUnit.ML, MeasureUnit.L,
  MeasureUnit.TSP, MeasureUnit.TBSP,
  MeasureUnit.PCS
)
