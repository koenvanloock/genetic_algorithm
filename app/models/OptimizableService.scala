package models

trait OptimizableService[Gene, Individual[Genes] <: Optimizable[Genes]] {

  def createRandomIndividual: Individual[Gene]
  def calculateFitness(genes: Gene) :Double
  def createIndividualFromGenes(genes: Gene): Individual[Gene]
  def createChild(parentOne: Individual[Gene], parentTwo: Individual[Gene]): Individual[Gene]
  def mutateChild(mutationPercentage: Double, mutationThreshold: Int, trip: Individual[Gene]): Individual[Gene]

  def getGeneStringFromIndividual(individual: Individual[Gene]): String

}
