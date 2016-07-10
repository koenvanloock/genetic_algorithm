package services

import javax.inject.Inject

import models.{BackpackWithSelectionChance, Backpack}

import scala.util.Random

class GeneticAlgorithmService @Inject()(backpackService: BackpackService) {
  type Generation = List[Backpack]
  type GenerationWithSelectionChance = List[BackpackWithSelectionChance]
  type Population = List[Generation]

  val NUMBER_OF_GENES = 15
  val GENERATIONSIZE  = 100
  val MIMIMUM_STANDARD_DEVIATION = 0.2
  val MUTATION_PERCENTAGE = 0.05
  val MAX_WEIGHT = 750
  val DISTRIBUTION_SCALE = 1000
  val NUMBER_OF_GENERATIONS = 200

  var population: Population = List()


  def drawInitialGeneration: Generation = (0 until GENERATIONSIZE).map{ x => backpackService.createRandomBackpack}.toList

  def determineMatingChance(totalFitness: Double, generation: List[Backpack], cummulativeChance: Int=0, generationWithSelectionChance: GenerationWithSelectionChance = List()): GenerationWithSelectionChance = {
    if(generation.nonEmpty){

      val backpackSelectionChance = if(generation.length > 1) (cummulativeChance + DISTRIBUTION_SCALE * backpackService.calculateFitness(generation.head.genes) / totalFitness).toInt else DISTRIBUTION_SCALE
      val backpackToAdd = BackpackWithSelectionChance(generation.head.genes, generation.head.value, generation.head.weight, generation.head.fitness, backpackSelectionChance)
      determineMatingChance(totalFitness, generation.tail, backpackToAdd.selectionChance, backpackToAdd :: generationWithSelectionChance)
    } else{
      generationWithSelectionChance
    }
  }


  def drawNextGenerations(initialGeneration : Generation): Population = {
    population = List(initialGeneration.map(x => backpackService.createBackpack(x.genes)))
    (0 until NUMBER_OF_GENERATIONS).foreach{ index =>
      val nextGeneration = drawGeneration(population.last)
      population = nextGeneration :: population
    }

    population
  }

  def drawGeneration(previousGeneration: Generation): Generation = {
    val totalFitness = previousGeneration.map(pack => backpackService.calculateFitness(pack.genes)).sum
    val generationWithSelectionChance = determineMatingChance(totalFitness, previousGeneration.sortBy(_.fitness))

    (0 until GENERATIONSIZE).map{ index =>
      val parentOne = selectRandomBackpackFromGeneration(generationWithSelectionChance)
      val parentTwo = selectRandomBackpackFromGeneration(generationWithSelectionChance)

      createChild(parentOne, parentTwo)
    }.toList
  }


  def selectRandomBackpackFromGeneration(generation: GenerationWithSelectionChance): BackpackWithSelectionChance ={
    val chosenValue = Random.nextInt(DISTRIBUTION_SCALE)+1

    def selectBackpack(lowerBound: Int=0, remainingBackpacks: List[BackpackWithSelectionChance] = generation.sortBy(_.selectionChance)): BackpackWithSelectionChance = {
      if(remainingBackpacks.head.selectionChance >= chosenValue){
        remainingBackpacks.head
      }else{
        selectBackpack(remainingBackpacks.head.selectionChance, remainingBackpacks.tail)
      }
    }

    selectBackpack()
  }

  def createChild(parentOne: BackpackWithSelectionChance, parentTwo: BackpackWithSelectionChance): Backpack = {
    val splitStart = Random.nextInt(NUMBER_OF_GENES -1)
    val splitStop = splitStart + 1 + Random.nextInt(NUMBER_OF_GENES - splitStart - 1)

    def joinGenes(genesA: String, genesB: String, index: Int=0, result: String=""): String ={
      val charToAdd = if(index > splitStart && index <= splitStop){
        genesB.head
      }else{
        genesA.head
      }

      if(index >= NUMBER_OF_GENES-1){
        result
      }else{
        joinGenes(genesA.tail, genesB.tail,index + 1, result+ charToAdd)
      }
    }

    val newGenes = joinGenes(parentOne.genes,parentTwo.genes)
    backpackService.createBackpack(newGenes)
  }
}