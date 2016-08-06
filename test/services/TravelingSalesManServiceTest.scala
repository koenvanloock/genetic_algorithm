package services

import models.travelingsalesmanproblem.Trip
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import services.travelingsalesmanproblem.TravelingSalesManService

@RunWith(classOf[JUnitRunner])
class TravelingSalesManServiceTest extends PlaySpec{

  "TravelingSalesManService" should {
    val travelingSalesmanService = new TravelingSalesManService


    "detect missing cities" in {
      travelingSalesmanService.findMissingCities(List(1,2,3,5), List(4,3,1,2)) mustBe List(5)
    }

    "detect double cities" in {
      travelingSalesmanService.findDoubleCities(List(1,2,3,5,8), List(4,3,1,2,6)) mustBe List(4,6)
    }

    "create a child with all cities present" in {

      val parentOne = travelingSalesmanService.createIndividualFromGenes(List(1,4,3,7,8,5,6,2))
      val parentTwo = travelingSalesmanService.createIndividualFromGenes(List(8,2,3,5,7,4,1,6))
      val createdChild = travelingSalesmanService.createChild(parentOne, parentTwo)

      createdChild.genes.length mustBe 8
      createdChild.genes must contain(1)
      createdChild.genes must contain(2)
      createdChild.genes must contain(3)
      createdChild.genes must contain(4)
      createdChild.genes must contain(5)
      createdChild.genes must contain(6)
      createdChild.genes must contain(7)
      createdChild.genes must contain(8)
    }



  "calculate the total distance of a trip" in {
    val genes = List(1,4,3,7,8,5,6,2)

    travelingSalesmanService.calculateTotalDistance(genes) mustBe  28.376237211312286
  }
}
}
