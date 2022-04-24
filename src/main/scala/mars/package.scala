package object mars {
  type SortedSet[T] = scala.collection.immutable.SortedSet[T]
  val SortedSet: scala.collection.immutable.SortedSet.type = scala.collection.immutable.SortedSet

  type SortedMap[K, +V] = scala.collection.immutable.SortedMap[K, V]
  val SortedMap: scala.collection.immutable.SortedMap.type = scala.collection.immutable.SortedMap
}
