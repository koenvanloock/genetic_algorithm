package models.backpackproblem

case class Backpack(genes: String, value: Double, weight: Double, fitness: Double)
case class BackpackWithSelectionChance(genes: String, value: Double, weight: Double, fitness: Double, selectionChance: Int)