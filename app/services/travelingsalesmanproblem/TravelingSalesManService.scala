package services.travelingsalesmanproblem

import models.OptimizableService
import models.travelingsalesmanproblem.{CityFactory, Trip}
import services.ConfigService

import scala.util.Random

class TravelingSalesManService extends OptimizableService[List[Int], Trip]{
  override def createRandomIndividual: Trip[List[Int]] = createIndividualFromGenes(Random.shuffle((0 until 8).toList))

  def calculateTotalDistance(genes: List[Int], calculatedDistance: Double=0): Double = {
    val startingCity = CityFactory.cities.find(_.cityNr == genes.head).get
    val endingCity = CityFactory.cities.find(_.cityNr == genes.drop(1).head).get
    val currentTripDistance = calculatedDistance + calculateDistance(startingCity.xCoord, startingCity.yCoord, endingCity.xCoord, endingCity.yCoord)
    if(genes.tail.length > 1) calculateTotalDistance(genes.tail, currentTripDistance) else currentTripDistance
  }

  def calculateDistance(x1: Double, y1: Double, x2: Double,y2: Double) = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2))


  def calculateTime(genes: List[Int]): Double = calculateTotalDistance(genes) * 0.8 // token implementation

  override def createIndividualFromGenes(genes: List[Int]): Trip[List[Int]] = Trip(genes,calculateFitness(genes),calculateTotalDistance(genes), calculateTime(genes))

  override def getGeneStringFromIndividual(individual: Trip[List[Int]]): String = individual.genes.mkString(",")

  def mutateChild(mutationPercentage: Double, mutationThreshold: Int, trip: Trip[List[Int]]): Trip[List[Int]] = {
      if(mutationThreshold >100 - mutationPercentage * 100){
        val randomIndex = Random.nextInt(CityFactory.cities.length)
        val geneToShift = trip.genes.drop(randomIndex).head
        val randomVal = Random.nextInt(CityFactory.cities.length)
        val indexToShiftWith = trip.genes.indexOf(randomVal)
        val newGenes = trip.genes.zipWithIndex.map { case(gene: Int, index: Int) =>
          if (index == randomIndex) {
            randomVal
          } else if (index == indexToShiftWith) {
            geneToShift
          } else {
            gene
          }
        }
        createIndividualFromGenes(newGenes)
      }else{
        trip
      }
  }


  override def createChild(parentOne: Trip[List[Int]], parentTwo: Trip[List[Int]]): Trip[List[Int]] = {
    val splitStart = Random.nextInt(CityFactory.cities.length - 1)
    val splitStop = splitStart + 1 + Random.nextInt(CityFactory.cities.length - splitStart - 1)
    val crossOverA = parentOne.genes.slice(splitStart, splitStop)
    val crossOverB: List[Int] = parentTwo.genes.slice(splitStart, splitStop)
    val missingCities = findMissingCities(crossOverA, crossOverB)
    val doubleCities = findDoubleCities(crossOverA, crossOverB)

    def joinGenes(genesA: List[Int], genesB: List[Int], index: Int = 0, result: List[Int]= Nil): List[Int] = {
      if (index >= CityFactory.cities.length) {
        result
      } else {
        val cityNrToAdd = if (index > splitStart && index <= splitStop) {
          genesB.head
        } else {
          if(doubleCities.contains(genesA.head)){
            val test = missingCities.find(missingCity => !result.contains(missingCity)).get
            test
          }else{
            genesA.head
          }
        }
        joinGenes(genesA.tail, genesB.tail, index + 1, cityNrToAdd :: result)
      }
    }

    val newGenes = joinGenes(parentOne.genes, parentTwo.genes).reverse

    val mutationThreshold = Random.nextInt(100)+1
    mutateChild(ConfigService.getMutationPercentage, mutationThreshold, createIndividualFromGenes(newGenes))
  }

  def findDoubleCities(crossOverA: List[Int], crossOverB: List[Int]): List[Int] = {
    crossOverB.filter(potentialDoubleCity => !crossOverA.contains(potentialDoubleCity))
  }

  def findMissingCities(crossOverA: List[Int], crossOverB: List[Int]): List[Int] = {
    Random.shuffle(crossOverA.filter(potentialMissingCity => !crossOverB.contains(potentialMissingCity)))
  }

  override def calculateFitness(genes: List[Int]): Double = calculateTotalDistance(genes)
}
