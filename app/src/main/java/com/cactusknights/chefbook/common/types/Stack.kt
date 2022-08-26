package com.cactusknights.chefbook.common.types

typealias Stack<T> = List<T>

fun <T> Stack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null

typealias MutableStack<T> = MutableList<T>

fun <T> Stack<T>.toMutable(): MutableStack<T> = this.toMutableList()

inline fun <T> MutableStack<T>.push(item: T) = add(item)

fun <T> MutableStack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null

@JvmName("peekMutable")
fun <T> MutableStack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null
