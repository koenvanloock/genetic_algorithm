package services

import models.{BackpackWithSelectionChance, Trinket, Backpack}
import scala.util.Random

class BackpackService {
 val MAX_WEIGHT = 750
  val NUMBER_OF_GENES = 15
  val NUMBER_OF_INITIAL_GENES = 4

  def calculateValue(genes: String): Double = {
    genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.quality else 0 }.sum
  }

  def calculateWeight(genes: String): Double = {
    genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.cost else 0 }.sum
  }

  def calculateFitness(genes: String) = {
    val weight = calculateWeight(genes)
    if(weight > MAX_WEIGHT) 0.0 else calculateValue(genes)
  }

  def createRandomBackpack = {

    val chosenIndices = (0 until NUMBER_OF_INITIAL_GENES).map( attempt => Random.nextInt(NUMBER_OF_GENES))
    val builder= new StringBuilder

    (0 until NUMBER_OF_GENES).map( geneIndex =>
        if(chosenIndices.contains(geneIndex)){
          //todo filter out too heavy backpacks!
          builder.append("1")
        } else{
          builder.append("0")
        }
    )

    createBackpack(builder.toString)
  }

  def mutateChild(mutationPercentage: Double, mutationThreshHold: Int, child: Backpack) = {

    if(mutationThreshHold >=100 - mutationPercentage * 100){
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