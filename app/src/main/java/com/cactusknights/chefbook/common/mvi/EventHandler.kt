package com.cactusknights.chefbook.common.mvi

interface EventHandler<T> {
    fun obtainEvent(event: T)
}