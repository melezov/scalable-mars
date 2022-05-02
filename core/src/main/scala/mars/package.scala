import zio.*

package object mars extends mars.resource.ResourceConversions {
  type ListMap[K, +V] = scala.collection.immutable.ListMap[K, V]
  val ListMap: scala.collection.immutable.ListMap.type = scala.collection.immutable.ListMap
  type SortedMap[K, +V] = scala.collection.immutable.SortedMap[K, V]
  val SortedMap: scala.collection.immutable.SortedMap.type = scala.collection.immutable.SortedMap
  type SortedSet[T] = scala.collection.immutable.SortedSet[T]
  val SortedSet: scala.collection.immutable.SortedSet.type = scala.collection.immutable.SortedSet

  type Action = mars.action.Action
  val Action: mars.action.Action.type = mars.action.Action
  type ActionBonus = mars.action.ActionBonus
  val ActionBonus: mars.action.ActionBonus.type = mars.action.ActionBonus
  type ForcedAction = mars.action.ForcedAction
  val ForcedAction: mars.action.ForcedAction.type = mars.action.ForcedAction

  type Award = mars.board.Award
  val Award: mars.board.Award.type = mars.board.Award
  type Board = mars.board.Board
  val Board: mars.board.Board.type = mars.board.Board
  type Milestone = mars.board.Milestone
  val Milestone: mars.board.Milestone.type = mars.board.Milestone

  type Corporation = mars.corporation.Corporation
  val Corporation: mars.corporation.Corporation.type = mars.corporation.Corporation

  type Tag = mars.card.Tag
  val Tag: mars.card.Tag.type = mars.card.Tag
  type ProjectType = mars.card.ProjectType
  val ProjectType: mars.card.ProjectType.type = mars.card.ProjectType

  type MarsError = mars.error.MarsError

  type Game = mars.game.Game
  val Game: mars.game.Game.type = mars.game.Game
  type Seed = mars.game.Seed
  val Seed: mars.game.Seed.type = mars.game.Seed

  type Player = mars.player.Player
  val Player: mars.player.Player.type = mars.player.Player
  type Mat = mars.player.Mat
  val Mat: mars.player.Mat.type = mars.player.Mat

  type Resource = mars.resource.Resource
  val Resource: mars.resource.Resource.type = mars.resource.Resource
  type Resources = mars.resource.Resources

  type Adjacency = mars.tile.Adjacency
  val Adjacency: mars.tile.Adjacency.type = mars.tile.Adjacency
  type PlacedTile = mars.tile.PlacedTile
  type OwnedTile = mars.tile.OwnedTile
  val OwnedTile: mars.tile.OwnedTile.type = mars.tile.OwnedTile
  type UnownedTile = mars.tile.UnownedTile
  val UnownedTile: mars.tile.UnownedTile.type = mars.tile.UnownedTile
  type RowPos = mars.tile.RowPos
  val RowPos: mars.tile.RowPos.type = mars.tile.RowPos
  type Tile = mars.tile.Tile
  val Tile: mars.tile.Tile.type = mars.tile.Tile
}
