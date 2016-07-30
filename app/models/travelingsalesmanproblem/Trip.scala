package models.travelingsalesmanproblem

import models.Optimizable

case class Trip[Genes](genes: Genes, fitness: Double, distance: Double, time: Double) extends Optimizable[Genes]
