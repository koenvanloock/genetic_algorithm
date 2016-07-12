'use strict';

angular.module("SurvivalOfTheFittestApp").controller("algorithmController", ["$scope", "algorithmService", function($scope, algorithmService){

    algorithmService.getInitialConfig().then(function(response){
        $scope.generationSize = response.data.generationSize;
        $scope.mutationPercentage = response.data.mutationPercentage;
        $scope.maxWeight = response.data.maxWeight;
        $scope.numberOfGenerations = response.data.numberOfGenerations;
    });

    $scope.runAlgorithm = function(){
        algorithmService.setPopulationSize(0);
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
            algorithmService.getInitialConfig().then(function(response){
                $scope.generationSize = response.data.generationSize;
                $scope.mutationPercentage = response.data.mutationPercentage;
                $scope.maxWeight = response.data.maxWeight;
                $scope.numberOfGenerations = response.data.numberOfGenerations;
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
      algorithmService.getBackpacks($scope.query).then(function(result){
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
    }

} ]);
