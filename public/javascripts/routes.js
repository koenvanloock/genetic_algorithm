angular.module('SurvivalOfTheFittestApp')
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'assets/main/views/main.html',
                controller: 'mainController'
            }).when('/backpack', {
            templateUrl: 'assets/backpack/views/backpack.html',
            controller: 'algorithmController'
        }).when('/travelingsalesman', {
            templateUrl: 'assets/travelingsalesman/views/travelingsalesman.html',
            controller: 'travelingSalesmanAlgorithmController'
        }).otherwise({
            redirectTo: '/'
        });
    })
;