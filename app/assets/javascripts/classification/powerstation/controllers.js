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

    var ShowPowerStationCtrl = function ($scope, $routeParams, $q, $location, powerStationService, $log, powerUnitService, componentTypeService, componentService) {
        var deferred = $q.defer();
        var promise = deferred.promise;

        $scope.powerStation = {};
        $scope.componentTypes = {};

        $scope.getComponentTypes = function () {
            componentTypeService.componentTypesForPowerStations().then(function (eq) {
                $scope.componentTypes = eq;
                promise.then(function () {
                    for (var i = 0; i < $scope.componentTypes.length; i++) {
                        $scope.componentTypes[i].component = $scope.getComponent($scope.componentTypes[i]);
                        if ($scope.componentTypes[i].component !== null) {
                            $log.debug("value for componenttype " + $scope.componentTypes[i].name + ": " + $scope.componentTypes[i].component.id);
                        }
                    }
                });
            });
        };

        $scope.getComponent = function (componentType) {
            var index = $scope.getComponentIndex(componentType);
            if (index !== null) {
                $log.debug("found component for componentType " + componentType.name + ": " + $scope.powerStation.components[index].id);
                return $scope.powerStation.components[index];
            }
            return null;
        };

        $scope.getComponentIndex = function (componentType) {
            for (var i = 0; i < $scope.powerStation.components.length; i++) {
                if ($scope.powerStation.components[i].componentType.id === componentType.id) {
                    return i;
                }
            }
            return null;

        };

        $scope.updateComponent = function (newcomponent, componentType) {
            if (componentType.component !== null) {
                componentService.updateComponentforPowerStation($scope.powerStation.id, componentType.component);
            }
            else {
                componentService.updateComponentForPowerStationByValues($scope.powerStation.id, componentType.id, newcomponent).then(function (newcomponentObject) {
                    $scope.powerStation.components.push(newcomponentObject);
                    componentType.component = newcomponentObject;
                });
            }
            return false;
        };


        $scope.getPowerStation = function () {
            powerStationService.powerStation($routeParams.id).then(function (eq) {
                $scope.powerStation = eq;
                deferred.resolve();
            });
        };




        $scope.createPowerUnit = function (powerUnit) {
            powerUnit.components = [];
            powerUnit.downtimeCosts = [];
            powerUnitService.createPowerUnit($scope.powerStation.id, powerUnit).then(function (eq) {
                $scope.powerStation.powerUnits.push(eq);
            });
        };

        $scope.getComponentTypes();
        $scope.getPowerStation();


    };
    ShowPowerStationCtrl.$inject = ['$scope', '$routeParams', '$q', '$location', 'powerStationService', '$log', 'powerUnitService', 'componentTypeService', 'componentService'];

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
