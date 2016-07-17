package services

import javax.inject.Inject

import models.backpackproblem.{BackpackWithSelectionChance, Backpack}
import services.backpackproblem.BackpackService

import scala.util.Random

class BackpackAlgorithmService @Inject()(optimizableService: BackpackService) {


  type Generation = List[Backpack]
  type GenerationWithSelectionChance = List[BackpackWithSelectionChance]
  type Population = List[Generation]

  var population: Population = List()

  def drawInitialGeneration: Generation = (0 until ConfigService.getGenerationSize).map { x => optimizableService.createRandomIndividual }.toList

  def determineMatingChance(totalFitness: Double, generation: List[Backpack], cummulativeChance: Int = 0, generationWithSelectionChance: GenerationWithSelectionChance = List()): GenerationWithSelectionChance = {
    if (generation.nonEmpty) {

      val backpackSelectionChance = if (generation.length > 1) (cummulativeChance + ConfigService.DISTRIBUTION_SCALE * optimizableService.calculateFitness(generation.head.genes) / totalFitness).toInt else ConfigService.DISTRIBUTION_SCALE
      val backpackToAdd = BackpackWithSelectionChance(generation.head.genes, generation.head.value, generation.head.weight, generation.head.fitness, backpackSelectionChance)
      determineMatingChance(totalFitness, generation.tail, backpackToAdd.selectionChance, backpackToAdd :: generationWithSelectionChance)
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
      val parentOne = selectRandomBackpackFromGeneration(generationWithSelectionChance)
      val parentTwo = selectRandomBackpackFromGeneration(generationWithSelectionChance)

      optimizableService.createChild(optimizableService.createIndividualFromGenes(parentOne.genes), optimizableService.createIndividualFromGenes(parentTwo.genes))
    }.toList
  }


  def selectRandomBackpackFromGeneration(generation: GenerationWithSelectionChance): BackpackWithSelectionChance = {
    val chosenValue = Random.nextInt(ConfigService.DISTRIBUTION_SCALE) + 1

    def selectBackpack(lowerBound: Int = 0, remainingBackpacks: List[BackpackWithSelectionChance] = generation.sortBy(_.selectionChance)): BackpackWithSelectionChance = {
      if (remainingBackpacks.head.selectionChance >= chosenValue) {
        remainingBackpacks.head
      } else {
        selectBackpack(remainingBackpacks.head.selectionChance, remainingBackpacks.tail)
      }
    }

    selectBackpack()
  }



  def getMaxes = population.map(generation => generation.map(_.fitness).max)

  def calculateProgressiveAvg: (Generation) => Double = {
    generation => generation.foldLeft((0.0, 1))((t: (Double, Int), pack: Backpack) => ( ( (t._2.toDouble-1) * t._1 + pack.fitness) / t._2.toDouble, t._2 + 1))._1
  }

  def getAverages: List[Double] = population.map(calculateProgressiveAvg)

  def getRequestedBackPacks(generationNumber: Int, page: Int, limit: Int): List[Backpack] = {
    population.drop(generationNumber).headOption.map(generation => generation.slice((page - 1) * limit, (page - 1) * limit + limit)).getOrElse(List())
  }
}