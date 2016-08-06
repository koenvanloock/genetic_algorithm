package services.travelingsalesmanproblem

import javax.inject.Inject

import models.travelingsalesmanproblem.Trip
import services.GenericAlgorithmService

class TravelingAlgorithmService @Inject()(travelingSalesManService: TravelingSalesManService)
  extends GenericAlgorithmService[Trip, List[Int]](travelingSalesManService){

  override def getMaxes = population.map(generation => generation.map(_.distance).min)

  def calculateProgressiveAvgDist: (List[(Trip[List[Int]])]) => Double = {
    generation => generation.foldLeft((0.0, 1))((t: (Double, Int), pack: Trip[List[Int]]) => ( ( (t._2.toDouble-1) * t._1 + pack.distance) / t._2.toDouble, t._2 + 1))._1
  }

  override def getAverages: List[Double] = population.map(calculateProgressiveAvgDist)
}
