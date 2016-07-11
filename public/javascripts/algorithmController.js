'use strict';

angular.module("SurvivalOfTheFittestApp").controller("algorithmController", ["$scope", "algorithmService", function($scope, algorithmService){

    $scope.population = [];
    $scope.generationNumbers = [];

    algorithmService.drawPopulation().then(function(){
        //$scope.population = response.data;

        algorithmService.getPopulationSize().then(function(response){
            $scope.populationSize = response.data.populationSize;
            algorithmService.setPopulationSize($scope.populationSize);
            for(var i=0; i< $scope.populationSize;i++){
                $scope.generationNumbers.push(i);
            }
        });
        algorithmService.getGenerationSize().then(function(response){
           $scope.generationSize = response.data.generationSize;
            $scope.getBackpacks();
        });

    });

    $scope.query = {
        limit: 5,
        page: 1,
        generationNr: 0
    };

    $scope.getBackpacks = function(){
      algorithmService.getBackpacks($scope.query).then(function(result){
          $scope.backpacks = result
      })
    };

    $scope.getBackpacks();

} ]);
