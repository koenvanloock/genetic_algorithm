package services

import models.backpackproblem.Trinket
import org.scalatestplus.play.PlaySpec
import services.backpackproblem.BackpackService

class BackpackServiceTest extends PlaySpec{

  "BackpackService" should {
      val backPackService = new BackpackService

    "calculate value of a geneString the correct item value" in {
      backPackService.calculateValue("110100000000000") mustBe 424
    }

    "calculate the weight of a geneString" in {
      backPackService.calculateWeight("110100000000000") mustBe 223
    }

    "calculate the fitness of an individual below weight threshold. It equals the value" in {
      backPackService.calculateFitness("110100000000000") mustBe 424
    }

    "calculate the fitness of an overweigth individual it is 0" in{
      backPackService.calculateFitness("111111111111000") mustBe 0
    }

    "create a random backpack with the correct gene length" in {
      backPackService.createRandomIndividual.genes.length mustBe ConfigService.NUMBER_OF_GENES
    }

    "create a random backpack, it has selected trinkets" in {
      val genes = backPackService.createRandomIndividual.genes
      val charLength = genes.length - genes.replace("1","").length
      charLength mustBe  >(0)
    }

    "return all the trinkets of a Backpack" in {
      val backpack = backPackService.createIndividualFromGenes("100100100001001")
      backPackService.getBackpackTrinkets(backpack) mustBe List(Trinket(70.0,135.0,"Zonnebril"), Trinket(80.0,150.0,"Schoenen"), Trinket(90.0,173.0,"Armband"), Trinket(113.0,214.0,"Boek"), Trinket(120.0,240.0,"Blok goud"))
    }

    "create child has a crossover gene" in {
      val parentOne = backPackService.createIndividualFromGenes("110011001110101")
      val parentTwo = backPackService.createIndividualFromGenes("001100110001010")

      val createdChild = backPackService.createChild(parentOne, parentTwo)

      createdChild.genes must not equal parentOne.genes
      createdChild.genes must not equal parentTwo.genes
      createdChild.genes.length mustBe ConfigService.NUMBER_OF_GENES
    }

    "mutate a given backpack with mutating threshhold" in {
      val backpack = backPackService.createIndividualFromGenes("100100100001001")
      val mutatedPackGenes = backPackService.mutateChild(0.05, 95, backpack).genes
      mutatedPackGenes must not equal backpack.genes
      mutatedPackGenes.length mustBe ConfigService.NUMBER_OF_GENES
    }

    "not mutate when the threshold is too low" in {
      val backpack = backPackService.createIndividualFromGenes("100100100001001")
      val mutatedPackGenes = backPackService.mutateChild(0.05, 94, backpack).genes
      mutatedPackGenes mustBe backpack.genes
    }

  }

}
