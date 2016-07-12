package services

import com.google.inject.Inject
import models.{BackpackWithSelectionChance, Trinket, Backpack}
import scala.util.Random

class BackpackService {

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

  def createRandomBackpack = {

    val chosenIndices = (0 until ConfigService.NUMBER_OF_INITIAL_GENES).map( attempt => Random.nextInt(ConfigService.NUMBER_OF_GENES))
    val builder= new StringBuilder

    (0 until ConfigService.NUMBER_OF_GENES).map( geneIndex =>
        if(chosenIndices.contains(geneIndex)){
          builder.append("1")
        } else{
          builder.append("0")
        }
    )

    createBackpack(builder.toString)
  }

  def createChild(parentOne: BackpackWithSelectionChance, parentTwo: BackpackWithSelectionChance): Backpack = {
    val splitStart = Random.nextInt(ConfigService.NUMBER_OF_GENES - 1)
    val splitStop = splitStart + 1 + Random.nextInt(ConfigService.NUMBER_OF_GENES - splitStart - 1)

    def joinGenes(genesA: String, genesB: String, index: Int = 0, result: String = ""): String = {


      if (index >= ConfigService.NUMBER_OF_GENES) {
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
    mutateChild(ConfigService.getMutationPercentage, mutationThreshold,createBackpack(newGenes))
  }

  def mutateChild(mutationPercentage: Double, mutationThreshold: Int, child: Backpack) = {

    if(mutationThreshold >100 - mutationPercentage * 100){
      val randomIndex = Random.nextInt(ConfigService.NUMBER_OF_GENES)
      val newGenes: String = child.genes
        .zipWithIndex
        .map{ case (gene: Char, index: Int) =>
          if(index == randomIndex){
            if(gene == '0') '1' else '0'
          } else {
            gene
          }
      }.foldLeft[String]("")((agg: String, charToAdd: Char) => agg + charToAdd)
      createBackpack(newGenes)
    }else{
      child
    }
  }

  def createBackpack(newGenes: String): Backpack = {
    Backpack(newGenes, calculateValue(newGenes), calculateWeight(newGenes), calculateFitness(newGenes))
  }

  def createBackpackWithSelectionChance(newGenes: String, selectionChance: Int): BackpackWithSelectionChance = {
    BackpackWithSelectionChance(newGenes, calculateValue(newGenes), calculateWeight(newGenes), calculateFitness(newGenes), selectionChance)
  }

  def getBackpackTrinkets(backpack: Backpack): List[Trinket] = {
    backpack.genes.zipWithIndex.flatMap { case (gene: Char, index: Int) => if (gene == '1') TrinketFactory.trinkets.drop(index).headOption else None }.toList
  }
}