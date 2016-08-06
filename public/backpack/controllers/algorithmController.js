'use strict';

angular.module("SurvivalOfTheFittestApp").controller("algorithmController", ["$scope","$location", "algorithmService", function($scope,$location, algorithmService){

    var problemName = "backpacks";

    algorithmService.getMaxWeight().then(function(response){
        $scope.maxWeight = response.data;
    });

    $scope.runAlgorithm = function(){
        algorithmService.setPopulationSize(0);
        $scope.population = [];
        $scope.generationNumbers = [];

        algorithmService.drawPopulation(problemName).then(function(){
            //$scope.population = response.data;

            algorithmService.getPopulationSize(problemName).then(function(response){
                $scope.populationSize = response.data.populationSize;
                algorithmService.setPopulationSize($scope.populationSize);
                for(var i=0; i< $scope.populationSize;i++){
                    $scope.generationNumbers.push(i);
                }
            });
            algorithmService.getMaxWeight().then(function(response){
                $scope.maxWeight = response.data;
                $scope.getBackpacks();
            });

        });

    };

    $scope.query = {
        limit: 5,
        page: 1,
        generationNr: 0
    };

    $scope.getBackpacks = function(){
      algorithmService.getIndividuals(problemName, $scope.query).then(function(result){
          $scope.backpacks = result
      })
    };

    $scope.updateMaxWeight = function(){
        algorithmService.setMaxWeight($scope.maxWeight);
    };

    $scope.updateMutationPercentage = function(){
        algorithmService.setMutationPercentage($scope.mutationPercentage);
    };

    $scope.updateGenerationSize = function(){
        algorithmService.setGenerationSize($scope.generationSize);
    };

    $scope.updateNumberOfGenerations = function(){
        algorithmService.setNumberOfGenerations($scope.numberOfGenerations);
    };

    $scope.gotoHome = function(){
        $location.path("/");
    }

} ]);
