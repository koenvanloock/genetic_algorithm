package controllers

import javax.inject.Inject

import models.Backpack
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.GeneticAlgorithmService

class AlgorithmController @Inject()(algorithmService: GeneticAlgorithmService) extends Controller{

  def runAlgorithm = Action{
    val initialGeneration = algorithmService.drawInitialGeneration
    val population = algorithmService.drawNextGenerations(initialGeneration)
    Ok(populationToJson(population))
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
}