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
        getInitialConfig: function(){
            return $http.get(base.url + "/initialConfig");
        },
        getChartData: function(){
            return $http.get(base.url + "/chartdata")
        },
        getLocalPopulationSize: function(){return populationSize;},
        setPopulationSize: function(popSize){ populationSize = popSize;},

        setMutationPercentage: function(percentage){
            return $http.post(base.url + "/mutationpercentage/"+percentage);
        },
        setMaxWeight: function(maxWeight){
            return $http.post(base.url + "/maxweight/"+maxWeight);
        },
        setGenerationSize: function(generationSize){
            return $http.post(base.url + "/generationsize/"+generationSize);
        },
        setNumberOfGenerations: function(numberOfGenerations){
            return $http.post(base.url + "/numberofgenerations/"+numberOfGenerations)
        }
    };
}
]);