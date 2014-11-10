define([], function () {
    'use strict';

    var CreatePowerStationCtrl = function ($scope, $location, powerStationService) {

        $scope.createPowerStation = function (powerStation) {
            powerStation.powerUnits = [];
            powerStationService.createPowerStation(powerStation).then(function (eq) {
                $location.path('/powerstations/' + eq.id);
            });
        };

        $scope.getPowerStation = function (id) {
            powerStationService.powerStation(id).then(function (eq) {
                return eq;
            });
        };

    };
    CreatePowerStationCtrl.$inject = ['$scope', '$location', 'powerStationService'];

    var ShowPowerStationCtrl = function ($scope, $routeParams, $q, $location, powerStationService, $log, powerUnitService) {

        $scope.powerStation = {};

        $scope.getPowerStation = function () {
            powerStationService.powerStation($routeParams.id).then(function (eq) {
                $scope.powerStation = eq;
            });
        };

        $scope.getPowerStation();


        $scope.createPowerUnit = function (powerUnit) {
            powerUnit.components = [];
            powerUnit.downtimeCosts = [];
            powerUnitService.createPowerUnit($scope.powerStation.id, powerUnit).then(function (eq) {
                $scope.powerStation.powerUnits.push(eq);
            });
        };


    };
    ShowPowerStationCtrl.$inject = ['$scope', '$routeParams', '$q', '$location', 'powerStationService', '$log', 'powerUnitService'];

    var ShowPowerStationsCtrl = function ($scope, $location, powerStationService) {
        $scope.powerStations = {};
        powerStationService.powerStations().then(function (eq) {
            $scope.powerStations = eq;
        });

        $scope.createPowerStation = function () {
            $location.path('/powerstations_create');
        };


        $scope.show = function (powerStation) {
            powerStationService.powerStation(powerStation.id).then(function (eq) {
                $location.path('/powerstations/' + eq.id);
            });
        };


    };
    ShowPowerStationsCtrl.$inject = ['$scope', '$location', 'powerStationService'];


    return {
        CreatePowerStationCtrl: CreatePowerStationCtrl,
        ShowPowerStationsCtrl: ShowPowerStationsCtrl,
        ShowPowerStationCtrl: ShowPowerStationCtrl
    };

});
