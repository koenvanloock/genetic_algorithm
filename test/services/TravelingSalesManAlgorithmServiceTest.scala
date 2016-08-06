package services

import models.travelingsalesmanproblem.Trip
import services.travelingsalesmanproblem.{TravelingAlgorithmService, TravelingSalesManService}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec

@RunWith(classOf[JUnitRunner])
class TravelingSalesManAlgorithmServiceTest extends PlaySpec{
  "TravelingAlgorithmService" should {
    val travelingSalesManService = new TravelingSalesManService()
    val geneticAlgorithmService = new TravelingAlgorithmService(travelingSalesManService)


    "draw a random backpack from a genereration" in {
      val generation = List(travelingSalesManService.createTripWithSelectionChance(List(1,3,5,8,4,6,2,7), 135),travelingSalesManService.createTripWithSelectionChance(List(4,7,5,1,3,2,6,8),650),travelingSalesManService.createTripWithSelectionChance(List(8,7,5,1,4,2,6,3),1000))
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
      nextGeneration.drop(10).head.genes.length mustBe travelingSalesManService.NUMBER_OF_GENES
    }


    "draw all generations" in {
      val initialGeneration  = geneticAlgorithmService.drawInitialGeneration
      val population = geneticAlgorithmService.drawNextGenerations(initialGeneration)
      population.length mustBe 201
      population.drop(33).head.length mustBe ConfigService.getGenerationSize
    }

    "calculate the avg of a generation" in {
      val population = List(List(Trip(List(1,3,5,8,4,6,2,7),514,263,514), Trip(List(4,7,5,1,3,2,6,8),274,143,274)))
      geneticAlgorithmService.population = population
      geneticAlgorithmService.getAverages mustBe List(394.0)
    }
  }

}
