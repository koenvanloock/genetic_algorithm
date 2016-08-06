package services.backpackproblem

import models.OptimizableService
import models.backpackproblem.{Backpack, Trinket, TrinketFactory}
import services.ConfigService

import scala.util.Random

class BackpackService extends OptimizableService[String,Backpack]{

  val NUMBER_OF_GENES = TrinketFactory.trinkets.length
  
  def calculateValue(genes: String): Double = {
    genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.quality else 0 }.sum
  }

  def calculateWeight(genes: String): Double = {
    genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.cost else 0 }.sum
  }

  def calculateFitness(genes: String) = {
    val weight = calculateWeight(genes)
    if(weight > ConfigService.getMaxWeight) 0.0 else calculateValue(genes)
  }

  def createRandomIndividual: Backpack[String] = {

    val chosenIndices = (0 until ConfigService.NUMBER_OF_INITIAL_GENES).map( attempt => Random.nextInt(NUMBER_OF_GENES))
    val builder= new StringBuilder

    (0 until NUMBER_OF_GENES).map( geneIndex =>
        if(chosenIndices.contains(geneIndex)){
          builder.append("1")
        } else{
          builder.append("0")
        }
    )

    createIndividualFromGenes(builder.toString)
  }

  def createChild(parentOne: Backpack[String], parentTwo: Backpack[String]): Backpack[String] = {
    val splitStart = Random.nextInt(NUMBER_OF_GENES - 1)
    val splitStop = splitStart + 1 + Random.nextInt(NUMBER_OF_GENES - splitStart - 1)

    def joinGenes(genesA: String, genesB: String, index: Int = 0, result: String = ""): String = {


      if (index >= NUMBER_OF_GENES) {
        result
      } else {
        val charToAdd = if (index > splitStart && index <= splitStop) {
          genesB.head
        } else {
          genesA.head
        }
        joinGenes(genesA.tail, genesB.tail, index + 1, result + charToAdd)
      }
    }

    val newGenes = joinGenes(parentOne.genes, parentTwo.genes)

    val mutationThreshold = Random.nextInt(100)+1
    mutateChild(ConfigService.getMutationPercentage, mutationThreshold, createIndividualFromGenes(newGenes))
  }

  def mutateChild(mutationPercentage: Double, mutationThreshold: Int, child: Backpack[String]) = {

    if(mutationThreshold >=100 - mutationPercentage * 100){
      val randomIndex = Random.nextInt(NUMBER_OF_GENES)
      val newGenes: String = child.genes
        .zipWithIndex
        .map{ case (gene: Char, index: Int) =>
          if(index == randomIndex){
            if(gene == '0') '1' else '0'
          } else {
            gene
          }
      }.foldLeft[String]("")((agg: String, charToAdd: Char) => agg + charToAdd)
      createIndividualFromGenes(newGenes)
    }else{
      child
    }
  }

  def createIndividualFromGenes(newGenes: String): Backpack[String] = {
    Backpack(newGenes, calculateValue(newGenes), calculateWeight(newGenes), calculateFitness(newGenes))
  }

  def createBackpackWithSelectionChance(newGenes: String, selectionChance: Double): (Backpack[String], Double) = {
    (createIndividualFromGenes(newGenes), selectionChance)
  }

  def getBackpackTrinkets(backpack: Backpack[String]): List[Trinket] = {
    backpack.genes.zipWithIndex.flatMap { case (gene: Char, index: Int) => if (gene == '1') TrinketFactory.trinkets.drop(index).headOption else None }.toList
  }
  def getGeneStringFromIndividual(individual: Backpack[String]): String = individual.genes

}