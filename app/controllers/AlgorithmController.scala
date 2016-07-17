package controllers

import javax.inject.Inject
import models.backpackproblem.Backpack
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.BackpackAlgorithmService

class AlgorithmController @Inject()(algorithmService: BackpackAlgorithmService) extends Controller{

  def runAlgorithm = Action{
    val initialGeneration = algorithmService.drawInitialGeneration
    val population = algorithmService.drawNextGenerations(initialGeneration)
    //Ok(populationToJson(population))
    Ok
  }


  def populationToJson(population: List[List[Backpack]]) = {
    Json.toJson( population.map{ generation => Json.toJson(generation.map(Json.toJson(_)(Json.writes[Backpack])))}    )

  }

  def getBackpacksOfGeneration(generationNr: Option[Int],page: Option[Int], limit: Option[Int]) = Action{
      val resolvedLimit = limit.getOrElse(15)
      val resolvedPage = page.getOrElse(1)
      val resolvedGenerationNumber = generationNr.getOrElse(0)

      val backpacks: List[Backpack] = algorithmService.getRequestedBackPacks(resolvedGenerationNumber, resolvedPage, resolvedLimit)

      Ok(Json.toJson(backpacks.map(Json.toJson(_)(Json.writes[Backpack]))))
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
}