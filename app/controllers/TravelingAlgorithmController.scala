package controllers

import javax.inject.Inject

import models.travelingsalesmanproblem.Trip
import play.api.libs.json.{JsValue, Writes, Json}
import play.api.mvc.{Action, Controller}
import services.travelingsalesmanproblem.TravelingAlgorithmService

class TravelingAlgorithmController @Inject()(algorithmService: TravelingAlgorithmService) extends Controller{

  def runAlgorithm = Action{
    val initialGeneration = algorithmService.drawInitialGeneration
    //val population =
    algorithmService.drawNextGenerations(initialGeneration)
    //Ok(populationToJson(population))
    Ok
  }


  def populationToJson(population: List[List[Trip[List[Int]]]]) = {
    Json.toJson( population.map{ generation => Json.toJson(generation.map(Json.toJson(_)(tripWrites)))}    )

  }

  def getTripsOfGeneration(generationNr: Option[Int], page: Option[Int], limit: Option[Int]) = Action{
    val resolvedLimit = limit.getOrElse(15)
    val resolvedPage = page.getOrElse(1)
    val resolvedGenerationNumber = generationNr.getOrElse(0)

    val backpacks: List[Trip[List[Int]]] = algorithmService.getRequestedBackPacks(resolvedGenerationNumber, resolvedPage, resolvedLimit)

    Ok(Json.toJson(backpacks.map(Json.toJson(_)(tripWrites))))
  }

  def getPopulationSize = Action(Ok(Json.obj("populationSize"->algorithmService.population.length)))

  def getChartData = Action{
    val avgs = algorithmService.getAverages
    val maxs = algorithmService.getMaxes

    Ok(Json.obj(
      "avg" -> Json.toJson(avgs),
      "maxs"-> Json.toJson(maxs)
    ))

  }

  val tripWrites = new Writes[Trip[List[Int]]]{
    override def writes(trip: Trip[List[Int]]): JsValue = Json.obj(
      "genes" -> trip.genes,
      "fitness" -> trip.fitness,
      "distance" -> trip.distance,
      "time" -> trip.time
    )

  }
}
