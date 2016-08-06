'use strict';

angular.module("SurvivalOfTheFittestApp").controller("travelingSalesmanAlgorithmController", ["$scope","$location", "algorithmService", function($scope,$location, algorithmService){

    var problemName = "travelingsalesman";

    algorithmService.getUpperBound().then( function(response){
        $scope.upperBound = response.data;
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
            algorithmService.getUpperBound().then( function(response){
                $scope.upperBound = response.data;
                $scope.getTrips();
            });

        });

    };

    $scope.query = {
        limit: 5,
        page: 1,
        generationNr: 0
    };

    $scope.getTrips = function(){
        algorithmService.getIndividuals(problemName, $scope.query).then(function(result){
            $scope.trips = result
        })
    };

    $scope.updateUpperBound = function(){
        algorithmService.setUpdateUpperBound($scope.upperBound);
    };

    $scope.gotoHome = function(){
        $location.path("/");
    }

} ]);
