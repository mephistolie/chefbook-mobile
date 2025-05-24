package io.chefbook.libs.utils.lists

inline fun <T> List<T>.replaceOrAdd(
  element: T,
  predicate: (T) -> Boolean,
): List<T> {
  var replaced = false
  val modified = this.map { item ->
    if (predicate(item)) {
      replaced = true
      element
    } else {
      item
    }
  }
  return if (replaced) modified else modified.plus(element)
}
