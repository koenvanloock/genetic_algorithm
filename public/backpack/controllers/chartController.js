angular.module("SurvivalOfTheFittestApp").controller("chartController", ['$scope', 'algorithmService', function ($scope, algorithmService) {

    $scope.labels = [];
    $scope.series = ['Max.', 'Avg.'];
    $scope.data = [];
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };

        var problemName="backpacks";

        $scope.$watch(algorithmService.getLocalPopulationSize, function (newVal, oldVal) {
            if(newVal != oldVal){
            algorithmService.getPopulationSize(problemName).then(function (response) {
                $scope.labels = [];
            for (var i = 0; i < response.data.populationSize; i++) {
                if (i % 10 === 0) {
                    $scope.labels.push(i);
                }
            }

            algorithmService.getChartData(problemName).then(function (response) {
                var newData = [];

                var resultAvgs = [];
                var resultMaxs = [];
                response.data.avg.map(function(avg, index){
                    if(index%10 == 0) resultAvgs.push(avg);
                });

                response.data.maxs.map(function(avg, index){
                    if(index%10 == 0) resultMaxs.push(avg);
                });

                newData.push(resultMaxs);
                newData.push(resultAvgs);
                $scope.data = newData;
            })

        })

    }});

}]);