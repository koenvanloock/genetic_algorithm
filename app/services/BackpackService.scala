package services

import models.{Trinket, Backpack}

import scala.util.Random

class BackpackService {
 val MAX_WEIGHT = 750
  val NUMBER_OF_GENES = 15
  val NUMBER_OF_INITIAL_GENES = 5

  def calculateValue(backpack: Backpack): Double = {
    backpack.genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.quality else 0 }.sum
  }

  def calculateWeight(backpack: Backpack): Double = {
    backpack.genes.zipWithIndex.map{ case(gene: Char, index: Int) => if(gene == '1') TrinketFactory.trinkets.drop(index).head.cost else 0 }.sum
  }

  def calculateFitness(backpack: Backpack) = {
    val weight = calculateWeight(backpack)
    if(weight > MAX_WEIGHT) 0.0 else calculateValue(backpack)
  }

  def createRandomBackpack = {

    val chosenIndices = (0 to NUMBER_OF_INITIAL_GENES).map( attempt => Random.nextInt(NUMBER_OF_GENES))
    val builder= new StringBuilder

    (0 to NUMBER_OF_GENES).map( geneIndex =>
        if(chosenIndices.contains(geneIndex)){
          //todo filter out too heavy backpacks!
          builder.append("1")
        } else{
          builder.append("0")
        }
    )

    Backpack(builder.toString)
  }

  def mutateChild(mutationPercentage: Double, mutationThreshHold: Int, child: Backpack) = {

    if(mutationThreshHold >= 100 - mutationPercentage * 100){
      val randomIndex = Random.nextInt(NUMBER_OF_GENES)
      val newGenes = child.genes
        .zipWithIndex
        .map{ case (gene: Char, index: Int) =>
          if(index == randomIndex){
            if(gene == '0') '1' else '0'
          } else {
            gene
          }
      }.toString()

      Backpack(newGenes)
    }else{
      child
    }

  }


  def getBackpackTrinkets(backpack: Backpack): List[Trinket] = {
    backpack.genes.zipWithIndex.flatMap { case (gene: Char, index: Int) => if (gene == '1') TrinketFactory.trinkets.drop(index).headOption else None }.toList
  }

}
