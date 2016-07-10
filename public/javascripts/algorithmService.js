'use strict';
angular.module("SurvivalOfTheFittestApp").service("algorithmService", ["$http","base", function($http, base) {


    return {
        drawPopulation: function () {
            return $http.get(base.url + "/runalgorithm");
        },

        getBackpacks: function(queryJson){
            return $http.get(base.url + "/backpacks?=generationNr="+queryJson.generationNr+"&page="+queryJson.page+"&limit="+queryJson.limit);
        }
    };
}
]);