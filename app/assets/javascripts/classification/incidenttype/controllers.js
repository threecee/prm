define([], function() {
    'use strict';

    var CreateIncidentTypeCtrl = function($scope, $location, incidenttypeService) {

        $scope.createIncidentType = function(incidentType) {
            incidenttypeService.createIncidentType(incidentType).then(function(eq) {
                $location.path('/incidenttypes/' + eq.id );
            });
        };

        $scope.getIncidentType = function(id) {
            incidenttypeService.incidentType(id).then(function(eq) {
                return eq;
            });
        };

    };
    CreateIncidentTypeCtrl.$inject = ['$scope', '$location', 'incidenttypeService'];

    var ShowIncidentTypeCtrl = function($scope, $routeParams, $location, incidenttypeService, $log) {
        $scope.incidentType = {};
        incidenttypeService.incidentType($routeParams.id).then(function(eq) {
            $scope.incidentType = eq;
            $log.debug("IncidentType value: " + $scope.incidentType.value);
        });
    };
    ShowIncidentTypeCtrl.$inject = ['$scope', '$routeParams', '$location', 'incidenttypeService', '$log'];

    var ShowIncidentTypesCtrl = function($scope,  $location, incidenttypeService) {
        $scope.incidentTypes = {};
        incidenttypeService.incidentTypes().then(function(eq) {
            $scope.incidentTypes = eq;
        });


        $scope.delete = function(incidentType, index) {
            incidenttypeService.deleteIncidentType(incidentType.id).then(function() {
                $scope.incidentTypes.splice(index,1);
            });
        };


    };
    ShowIncidentTypesCtrl.$inject = ['$scope', '$location', 'incidenttypeService'];


    return {
        CreateIncidentTypeCtrl: CreateIncidentTypeCtrl,
        ShowIncidentTypesCtrl: ShowIncidentTypesCtrl,
        ShowIncidentTypeCtrl: ShowIncidentTypeCtrl
    };

});
