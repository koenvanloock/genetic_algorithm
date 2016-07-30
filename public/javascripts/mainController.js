'use strict';

angular.module("SurvivalOfTheFittestApp").controller("mainController", ["$scope", "$location", function($scope, $location){

    $scope.changeCase = function(caseName){
        $location.path("/"+caseName);
    };

    $scope.cases = [
        {
            caseName: "backpack",
            style: {
                'background-image':  'url("/assets/images/beach.jpg")',
                'background-repeat': 'no-repeat',
                "height": "250px",
                "width": "80%",
                "background-attachment": "fixed",
                "background-position": "center",
                "-webkit-background-size": "cover",
                "-moz-background-size": "cover",
                "-o-background-size": "cover",
                "background-size": "cover"
            }
        },

        {
            caseName: "travelingsalesman",
            style: {
                'background-image': 'url("/assets/images/travel.jpg")', 'background-repeat': 'no-repeat',
                "height": "250px",
                "width": "80%",
                "background-attachment": "fixed",
                "background-position": "center",
                "-webkit-background-size": "cover",
                "-moz-background-size": "cover",
                "-o-background-size": "cover",
                "background-size": "cover"
            }
        }

    ]
}]);
