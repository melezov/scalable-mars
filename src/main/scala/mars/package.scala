import zio.*

package object mars {
  type SortedMap[K, +V] = scala.collection.immutable.SortedMap[K, V]
  val SortedMap: scala.collection.immutable.SortedMap.type = scala.collection.immutable.SortedMap
  type SortedSet[T] = scala.collection.immutable.SortedSet[T]
  val SortedSet: scala.collection.immutable.SortedSet.type = scala.collection.immutable.SortedSet

  type MarsError = mars.error.MarsError

  type Action = mars.action.Action
  val Action: mars.action.Action.type = mars.action.Action
  type ActionBonus = mars.action.ActionBonus
  val ActionBonus: mars.action.ActionBonus.type = mars.action.ActionBonus

  type Award = mars.board.Award
  val Award: mars.board.Award.type = mars.board.Award

  type Player = mars.player.Player
  val Player: mars.player.Player.type = mars.player.Player
  type Mat = mars.player.Mat
  val Mat: mars.player.Mat.type = mars.player.Mat

  type Tile = mars.tile.Tile
  val Tile: mars.tile.Tile.type = mars.tile.Tile
  type PlacedTile = mars.tile.PlacedTile
  type OwnedTile = mars.tile.OwnedTile
  val OwnedTile: mars.tile.OwnedTile.type = mars.tile.OwnedTile
  type UnownedTile = mars.tile.UnownedTile
  val UnownedTile: mars.tile.UnownedTile.type = mars.tile.UnownedTile
  type RowPos = mars.tile.RowPos
  val RowPos: mars.tile.RowPos.type = mars.tile.RowPos
  type Adjacency = mars.tile.Adjacency
  val Adjacency: mars.tile.Adjacency.type = mars.tile.Adjacency
}
