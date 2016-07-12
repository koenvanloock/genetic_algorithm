package services

import play.api.Logger

object ConfigService {

  val NUMBER_OF_GENES = 15
  val DISTRIBUTION_SCALE = 1000

  val NUMBER_OF_INITIAL_GENES = 5

  private var generationSize = 100
  private var mutationPercentage = 0.05
  private var maxWeight = 750
  private var numberOfGenerations = 200



  def getGenerationSize = generationSize
  def setGenerationSize(generationSize: Int) = { this.generationSize = generationSize}

  def getMutationPercentage = mutationPercentage
  def setMutationPercentage(mutationPercentage: Double) = {this.mutationPercentage = mutationPercentage}

  def getMaxWeight = maxWeight
  def setMaxWeight(maxWeight: Int) = {this.maxWeight = maxWeight
    Logger.info("new maxWeight:"+maxWeight)
  }

  def getNumberOfGenerations = numberOfGenerations
  def setNumberOfGenerations(numberOfGenerations: Int) = { this.numberOfGenerations = numberOfGenerations
    Logger.info("new numberOfGenerations: "+numberOfGenerations)
  }
}
