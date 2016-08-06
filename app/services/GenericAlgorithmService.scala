package services

import javax.inject.Inject

import models.{OptimizableService, Optimizable}

import scala.util.Random

abstract class GenericAlgorithmService[Optimi[X] <: Optimizable[X], Genes] @Inject()(optimizableService: OptimizableService[Genes, Optimi]) {

  type Generation = List[Optimi[Genes]]
  type Population = List[Generation]
  type GenerationWithSelectionChance = List[(Optimi[Genes], Double)]

  def drawInitialGeneration: Generation = (0 until ConfigService.getGenerationSize).map { x => optimizableService.createRandomIndividual }.toList

  var population: Population = List()


  def determineMatingChance(totalFitness: Double, generation: List[Optimi[Genes]], cummulativeChance: Double = 0, generationWithSelectionChance: GenerationWithSelectionChance = List()): GenerationWithSelectionChance = {
    if (generation.nonEmpty) {

      val backpackSelectionChance = if (generation.length > 1) cummulativeChance + ConfigService.DISTRIBUTION_SCALE * optimizableService.calculateFitness(generation.head.genes) / totalFitness else ConfigService.DISTRIBUTION_SCALE
      val backpackToAdd = (generation.head, backpackSelectionChance)
      determineMatingChance(totalFitness, generation.tail, backpackToAdd._2, backpackToAdd :: generationWithSelectionChance)
    } else {
      generationWithSelectionChance
    }
  }

  def drawNextGenerations(initialGeneration: Generation): Population = {
    var population = List(initialGeneration.map(x => optimizableService.createIndividualFromGenes(x.genes)))
    (0 until ConfigService.getNumberOfGenerations).foreach { index =>
      val nextGeneration = drawGeneration(population.head)
      population = nextGeneration :: population
    }
    this.population = population.reverse
    population.reverse
  }

  def drawGeneration(previousGeneration: Generation): Generation = {
    val totalFitness = previousGeneration.map(pack => optimizableService.calculateFitness(pack.genes)).sum
    val generationWithSelectionChance = determineMatingChance(totalFitness, previousGeneration.sortBy(_.fitness))

    (0 until ConfigService.getGenerationSize).map { index =>
      val parentOne = selectRandomIndividualFromGeneration(generationWithSelectionChance)
      val parentTwo = selectRandomIndividualFromGeneration(generationWithSelectionChance)

      optimizableService.createChild(optimizableService.createIndividualFromGenes(parentOne._1.genes), optimizableService.createIndividualFromGenes(parentTwo._1.genes))
    }.toList
  }

  def selectRandomIndividualFromGeneration(generation: GenerationWithSelectionChance): (Optimi[Genes], Double) = {
    val chosenValue = Random.nextInt(ConfigService.DISTRIBUTION_SCALE) + 1

    def selectBackpack(lowerBound: Double = 0, remainingBackpacks: List[(Optimi[Genes], Double)] = generation.sortBy(_._2)): (Optimi[Genes], Double) = {
      if (remainingBackpacks.head._2 >= chosenValue) {
        remainingBackpacks.head
      } else {
        selectBackpack(remainingBackpacks.head._2, remainingBackpacks.tail)
      }
    }

    selectBackpack()
  }

  def getMaxes = population.map(generation => generation.map(_.fitness).max)

  def calculateProgressiveAvg: (Generation) => Double = {
    generation => generation.foldLeft((0.0, 1))((t: (Double, Int), pack: Optimi[Genes]) => ( ( (t._2.toDouble-1) * t._1 + pack.fitness) / t._2.toDouble, t._2 + 1))._1
  }

  def getAverages: List[Double] = population.map(calculateProgressiveAvg)

  def getRequestedBackPacks(generationNumber: Int, page: Int, limit: Int): List[Optimi[Genes]] = {
    population.drop(generationNumber).headOption.map(generation => generation.slice((page - 1) * limit, (page - 1) * limit + limit)).getOrElse(List())
  }

}
