package services.backpackproblem

import javax.inject.Inject

import models.backpackproblem.Backpack
import services.GenericAlgorithmService

class BackpackAlgorithmService @Inject()(backpackService: BackpackService)
  extends GenericAlgorithmService[Backpack, String](backpackService)
