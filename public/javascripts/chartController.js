angular.module("SurvivalOfTheFittestApp").controller("chartController", ['$scope', 'algorithmService', function ($scope, algorithmService) {

    $scope.labels = [];
    $scope.series = ['Max.', 'Avg.'];
    $scope.data = [
        [65, 59, 80, 81, 56, 55, 40],
        [28, 48, 40, 19, 86, 27, 90]
    ];
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };



        $scope.$watch(algorithmService.getLocalPopulationSize, function (newVal, oldVal) {
            algorithmService.getPopulationSize().then(function (response) {
            for (var i = 0; i < response.data.populationSize; i++) {
                if (i % 10 === 0) {
                    $scope.labels.push(i);
                }
            }
            algorithmService.getChartData().then(function (response) {
                $scope.data = [];
                $scope.data.push(response.data.maxs);
                $scope.data.push(response.data.avg);
            })
        })
    });
}]);