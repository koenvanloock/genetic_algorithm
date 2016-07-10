'use strict';

angular.module("SurvivalOfTheFittestApp").controller("algorithmController", ["$scope", "algorithmService", function($scope, algorithmService){

    $scope.population = [];
    $scope.generationNumbers = [];

    algorithmService.drawPopulation().then(function(response){
        $scope.population = response.data;
        for(var i=0; i<= $scope.population.length;i++){
            $scope.generationNumbers.push(i+1);
        }
        $scope.getBackpacks();
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
