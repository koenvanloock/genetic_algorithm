package services

trait OptimizableService[Individual, Genes] {

  def createRandomIndividual: Individual
  def calculateFitness(genes: Genes) :Double
  def createIndividualFromGenes(genes: Genes): Individual
  def createChild(parentOne: Individual, parentTwo: Individual): Individual
  def mutateChild(mutationPercentage: Double, mutationThreshold: Int, trip: Individual): Individual

  def getGeneStringFromIndividual(individual: Individual): String

}
