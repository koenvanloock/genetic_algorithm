var survivalOfTheFittest = angular.module("SurvivalOfTheFittestApp", ["ngMaterial","ngRoute","md.data.table","chart.js"]) .constant('base', {
    url: window.location.origin
});

survivalOfTheFittest.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

survivalOfTheFittest.directive('configSetup', function(){
    return{
        restrict: 'E',
        scope: {round: '='},
        templateUrl: "assets/main/directives/views/configSetup.html",
        controller: "configController"
    }
});



