package services

import models.backpackproblem.Backpack
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import services.backpackproblem.{BackpackAlgorithmService, BackpackService}

@RunWith(classOf[JUnitRunner])
class BackpackAlgorithmServiceTest extends PlaySpec{

  "BackpackAlgorithmService" should {
    val backpackService = new BackpackService()
    val geneticAlgorithmService = new BackpackAlgorithmService(backpackService)


    "draw a random backpack from a genereration" in {
      val generation  = List(backpackService.createBackpackWithSelectionChance("000001101110000",350),backpackService.createBackpackWithSelectionChance("110000101010000",650),backpackService.createBackpackWithSelectionChance("000000101010011",1000))
      val drawnList = (0 to 10).map(x => geneticAlgorithmService.selectRandomIndividualFromGeneration(generation)).toList.distinct
      drawnList.length mustBe >(1)
    }

    "draw an initial generation" in {
      val drawnGeneration = geneticAlgorithmService.drawInitialGeneration
      drawnGeneration.length mustBe ConfigService.getGenerationSize
    }


    "draw a next generation" in {
      val initialGeneration  = geneticAlgorithmService.drawInitialGeneration
      val nextGeneration = geneticAlgorithmService.drawGeneration(initialGeneration)
        nextGeneration.length mustBe ConfigService.getGenerationSize
      nextGeneration.drop(10).head.genes.length mustBe backpackService.NUMBER_OF_GENES
    }


    "draw all generations" in {
      val initialGeneration  = geneticAlgorithmService.drawInitialGeneration
      val population = geneticAlgorithmService.drawNextGenerations(initialGeneration)
      population.length mustBe 201
      population.drop(33).head.length mustBe ConfigService.getGenerationSize
    }

    "calculate the avg of a generation" in {
      val population = List(List(Backpack("110000000000001",514,263,514), Backpack("110000000000000",274,143,274)))
      geneticAlgorithmService.population = population
      geneticAlgorithmService.getAverages mustBe List(394.0)
    }
  }
}
