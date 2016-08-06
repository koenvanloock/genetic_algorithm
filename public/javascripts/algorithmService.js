'use strict';
angular.module("SurvivalOfTheFittestApp").service("algorithmService", ["$http","base", function($http, base) {
    var populationSize=0;

    return {
        drawPopulation: function (problemName) {
            return $http.get(base.url + "/" + problemName +"/runalgorithm");
        },

        getIndividuals: function(problemName, queryJson){
            return $http.get(base.url + "/" + problemName + "?=generationNr="+queryJson.generationNr+"&page="+queryJson.page+"&limit="+queryJson.limit);
        },

        getPopulationSize: function(problemName){
            return $http.get(base.url + "/" + problemName + "/populationsize");
        },
        getChartData: function(problemName){
            return $http.get(base.url + "/" + problemName + "/chartdata")
        },
        getInitialConfig: function(){
            return $http.get(base.url + "/initialConfig");
        },
        getLocalPopulationSize: function(){return populationSize;},
        setPopulationSize: function(popSize){ populationSize = popSize;},

        setMutationPercentage: function(percentage){
            return $http.post(base.url + "/mutationpercentage/"+percentage);
        },
        setMaxWeight: function(maxWeight){
            return $http.post(base.url + "/maxweight/"+maxWeight);
        },
        getMaxWeight: function(){
            return $http.get(base.url + "/maxweight");
        },
        setGenerationSize: function(generationSize){
            return $http.post(base.url + "/generationsize/"+generationSize);
        },
        setNumberOfGenerations: function(numberOfGenerations){
            return $http.post(base.url + "/numberofgenerations/"+numberOfGenerations)
        },
        setUpdateUpperBound: function(upperBound){
            return $http.post(base.url + "/upperbound/"+upperBound);
        },
        getUpperBound: function() {
            return $http.get(base.url + "/upperbound");
        }
    };
}
]);