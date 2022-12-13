package com.mysty.chefbook.core.mvi

interface EventHandler<T> {
    fun obtainEvent(event: T)
}
