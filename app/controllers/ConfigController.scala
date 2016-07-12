package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.ConfigService

class ConfigController @Inject()() extends Controller{

  def getInitialConfig = Action{
    Ok(Json.obj(
      "mutationPercentage"  -> ConfigService.getMutationPercentage,
      "maxWeight"           -> ConfigService.getMaxWeight,
      "generationSize"      -> ConfigService.getGenerationSize,
      "numberOfGenerations" -> ConfigService.getNumberOfGenerations
    ))
  }

  def setMutationPercentage(mutationPercentage: Double)  = Action{
    ConfigService.setMutationPercentage(mutationPercentage)
    Ok
  }

  def setMaxWeight(maxWeight: Int) = Action{
    ConfigService.setMaxWeight(maxWeight)
    Ok
  }

  def setGenerationSize(generationSize: Int) = Action{
    ConfigService.setGenerationSize(generationSize)
    Ok
  }

  def setNumberOfGenerations(numberOfGenerations: Int) = Action{
    ConfigService.setNumberOfGenerations(numberOfGenerations)
    Ok
  }

}
