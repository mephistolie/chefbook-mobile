package io.chefbook.design.components.pager

internal interface PagerStateBridge {
  val currentPage: Int
  val currentPageOffset: Float
}
