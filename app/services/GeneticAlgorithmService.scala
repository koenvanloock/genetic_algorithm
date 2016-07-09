package services

import javax.inject.Inject

import models.Backpack

class GeneticAlgorithmService @Inject()(backpackService: BackpackService) {
  type Generation = List[Backpack]
  type Population = List[Generation]

  var GENERATIONSIZE  = 100
  var MIMIMUM_STANDARD_DEVIATION = 0.2
  var MUTATION_PERCENTAGE = 0.05
  var MAX_WEIGHT = 750


  def drawInitialGeneration: Generation = (0 to GENERATIONSIZE).map{ x => backpackService.createRandomBackpack}.toList

  def drawNextGenerations(initialGeneration : Generation): Population = {
    List()
  }


}
