import scala.collection.immutable

package object mars /* extends mars.resource.ResourceImplicits */ {
  // --- ZIO ---

  type IO[+E, +A] = zio.IO[E, A]
  val ZIO: zio.ZIO.type = zio.ZIO

  // --- STDLIB ---

  type ListSet[T] = immutable.ListSet[T]
  val ListSet: immutable.ListSet.type = immutable.ListSet
  type ListMap[K, +V] = immutable.ListMap[K, V]
  val ListMap: immutable.ListMap.type = immutable.ListMap
  type SortedMap[K, +V] = immutable.SortedMap[K, V]
  val SortedMap: immutable.SortedMap.type = immutable.SortedMap
  type SortedSet[T] = immutable.SortedSet[T]
  val SortedSet: immutable.SortedSet.type = immutable.SortedSet

//  // --- MARS ---
//
  type Action = mars.action.Action
  val Action: mars.action.Action.type = mars.action.Action
  type ActionBonus = mars.action.ActionBonus
  val ActionBonus: mars.action.ActionBonus.type = mars.action.ActionBonus
  type BonusAction = mars.action.BonusAction
  type ForcedAction = mars.action.ForcedAction
  val ForcedAction: mars.action.ForcedAction.type = mars.action.ForcedAction
  type PlayerAction = mars.action.PlayerAction
  val PlayerAction: mars.action.PlayerAction.type = mars.action.PlayerAction
  type TurnOrderAction = mars.action.TurnOrderAction
  val TurnOrderAction: mars.action.TurnOrderAction.type = mars.action.TurnOrderAction
//
  type Award = mars.board.Award
  type Awards = mars.board.Awards
  val Awards: mars.board.Awards.type = mars.board.Awards
//  type Board = mars.board.Board
//  val Board: mars.board.Board.type = mars.board.Board
  type Milestone = mars.board.Milestone
  type Milestones = mars.board.Milestones
  val Milestones: mars.board.Milestones.type = mars.board.Milestones

  //  type Corporation = mars.corporation.Corporation
//  val Corporation = mars.corporation.Corporation
//
//  type Seed = mars.engine.Seed
//  type TurnOrder = mars.engine.TurnOrder
//  val TurnOrder = mars.engine.TurnOrder
//
  type MarsErr = mars.err.MarsErr
//
//  type Game = mars.game.Game
//  val Game = mars.game.Game
//  type GameStage = mars.game.GameStage
//  val GameStage = mars.game.GameStage
//
  type Color = mars.player.Color
  val Color: mars.player.Color.type = mars.player.Color
//  type Mat = mars.player.Mat
//  val Mat = mars.player.Mat
//  type Player = mars.player.Player
//  val Player = mars.player.Player
  type TurnState = mars.player.TurnState
  val TurnState: mars.player.TurnState.type = mars.player.TurnState

  type Tag = mars.project.Tag
  val Tag: mars.project.Tag.type = mars.project.Tag
  type ProjectType = mars.project.ProjectType
  val ProjectType: mars.project.ProjectType.type = mars.project.ProjectType

//  type Resource = mars.resource.Resource
//  val Resource = mars.resource.Resource
//  type Resources = mars.resource.Resources

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
