package services

import models.BackpackWithSelectionChance
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec

@RunWith(classOf[JUnitRunner])
class GeneticAlgorithmServiceTest extends PlaySpec{

  "GeneticAlgorithmService" should {
    val backpackService = new BackpackService
    val geneticAlgorithmService = new GeneticAlgorithmService(backpackService)


    "draw a random backpack from a genereration" in {
      val generation  = List(backpackService.createBackpackWithSelectionChance("000001101110000",350),backpackService.createBackpackWithSelectionChance("110000101010000",650),backpackService.createBackpackWithSelectionChance("000000101010011",1000))
      val drawnList = (0 to 10).map(x => geneticAlgorithmService.selectRandomBackpackFromGeneration(generation)).toList.distinct
      drawnList.length mustBe >(1)
    }

    "create child has a crossover gene" in {
      val parentOne = backpackService.createBackpackWithSelectionChance("110011001110101",250)
      val parentTwo = backpackService.createBackpackWithSelectionChance("001100110001010", 483)

      val createdChild = geneticAlgorithmService.createChild(parentOne, parentTwo)

      createdChild.genes must not equal parentOne.genes
      createdChild.genes must not equal parentTwo.genes
    }

    "draw an initial generation" in {
      val drawnGeneration = geneticAlgorithmService.drawInitialGeneration
      drawnGeneration.length mustBe geneticAlgorithmService.GENERATIONSIZE
    }


    "draw a next generation" in {
      val initialGeneration  = geneticAlgorithmService.drawInitialGeneration
      geneticAlgorithmService.drawGeneration(initialGeneration).length mustBe geneticAlgorithmService.GENERATIONSIZE
    }

    "draw all generations" in {
      val initialGeneration  = geneticAlgorithmService.drawInitialGeneration
      val population = geneticAlgorithmService.drawNextGenerations(initialGeneration)
      println(population.mkString("\n\n"))
      population.length mustBe 201
      population.drop(33).head.length mustBe geneticAlgorithmService.GENERATIONSIZE
    }
  }
}
