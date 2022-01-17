package com.cactusknights.chefbook.base

interface EventHandler<T> {
    fun obtainEvent(event: T)
}