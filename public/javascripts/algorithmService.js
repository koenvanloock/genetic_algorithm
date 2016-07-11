'use strict';
angular.module("SurvivalOfTheFittestApp").service("algorithmService", ["$http","base", function($http, base) {
    var populationSize=0;

    return {
        drawPopulation: function () {
            return $http.get(base.url + "/runalgorithm");
        },

        getBackpacks: function(queryJson){
            return $http.get(base.url + "/backpacks?=generationNr="+queryJson.generationNr+"&page="+queryJson.page+"&limit="+queryJson.limit);
        },

        getPopulationSize: function(){
            return $http.get(base.url + "/populationsize");
        },
        getGenerationSize: function(){
            return $http.get(base.url + "/generationsize")
        },
        getChartData: function(){
            return $http.get(base.url + "/chartdata")
        },
        getLocalPopulationSize: function(){return populationSize;},
        setPopulationSize: function(popSize){ populationSize = popSize;}
    };
}
]);