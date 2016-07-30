package models.backpackproblem

import models.Optimizable

case class Backpack[Genes](genes: Genes, value: Double, weight: Double, fitness: Double) extends Optimizable[Genes]
case class BackpackWithSelectionChance(genes: String, value: Double, weight: Double, fitness: Double, selectionChance: Int)