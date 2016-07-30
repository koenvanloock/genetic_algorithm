package models

trait Optimizable[Genes] {

  def genes :Genes
  def fitness: Double
}
