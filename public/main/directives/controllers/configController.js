angular.module("SurvivalOfTheFittestApp").controller("configController", ['$scope', 'algorithmService', function ($scope, algorithmService) {

    algorithmService.getInitialConfig().then(function(response){
        $scope.generationSize = response.data.generationSize;
        $scope.mutationPercentage = response.data.mutationPercentage;
        $scope.numberOfGenerations = response.data.numberOfGenerations;
    });

    $scope.updateMutationPercentage = function(){
        algorithmService.setMutationPercentage($scope.mutationPercentage);
    };

    $scope.updateGenerationSize = function(){
        algorithmService.setGenerationSize($scope.generationSize);
    };

    $scope.updateNumberOfGenerations = function(){
        algorithmService.setNumberOfGenerations($scope.numberOfGenerations);
    };
}]);