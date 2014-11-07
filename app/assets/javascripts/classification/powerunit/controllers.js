define([], function () {
    'use strict';

    var CreatePowerUnitCtrl = function ($scope, $location, powerUnitService) {

        $scope.createPowerUnit = function (powerUnit) {
            powerUnitService.createPowerUnit(powerUnit).then(function (eq) {
                $location.path('/powerunits/' + eq.id);
            });
        };


    };
    CreatePowerUnitCtrl.$inject = ['$scope', '$location', 'powerUnitService'];

    var ShowPowerUnitCtrl = function ($scope, $routeParams, $q, $location, powerUnitService, $log, componentTypeService, componentService, downtimecostService) {
        var deferred = $q.defer();
        var promise = deferred.promise;

        $scope.powerUnit = {};
        $scope.componentTypes = {};
        $scope.incidentTypes = {};



        $scope.getComponentTypes = function () {
            componentTypeService.componentTypes().then(function (eq) {
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


        $scope.getPowerUnit = function () {
            powerUnitService.powerUnit($routeParams.id).then(function (eq) {
                $scope.powerUnit = eq;
                deferred.resolve();
                $log.debug("PowerUnit value: " + $scope.powerUnit.name);
            });
        };

        $scope.getComponent = function (componentType) {
            var index = $scope.getComponentIndex(componentType);
            if (index !== null) {
                $log.debug("found component for componentType " + componentType.name + ": " + $scope.powerUnit.components[index].id);
                return $scope.powerUnit.components[index];
            }
            return null;
        };

        $scope.getComponentIndex = function (componentType) {
            for (var i = 0; i < $scope.powerUnit.components.length; i++) {
                if ($scope.powerUnit.components[i].componentType.id === componentType.id) {
                    return i;
                }
            }
            return null;

        };


        $scope.delete = function (powerUnit, index) {
            powerUnitService.deletePowerUnit(powerUnit.id).then(function () {
                $scope.powerUnits.splice(index, 1);
            });
        };
        $scope.update = function (powerUnit) {
            powerUnitService.updatePowerUnit(powerUnit.id).then(function () {
            });
        };

        $scope.updateComponent = function (newcomponent, componentType) {
            if (componentType.component !== null) {
                componentService.updateComponent($scope.powerUnit.id, componentType.component);
            }
            else {
                componentService.updateComponentByValues($scope.powerUnit.id, componentType.id, newcomponent).then(function (newcomponentObject) {
                    $scope.powerUnit.components.push(newcomponentObject);
                    componentType.component = newcomponentObject;
                });
            }
            return false;
        };
        $scope.updateDowntimeCost = function (data) {
                downtimecostService.updateDowntimeCost($scope.powerUnit.id, data);
            return false;
        };







        $scope.getComponentTypes();
        $scope.getPowerUnit();

    };
    ShowPowerUnitCtrl.$inject = ['$scope', '$routeParams', '$q', '$location', 'powerUnitService', '$log', 'componentTypeService', 'componentService', 'downtimecostService'];

    var ShowPowerUnitsCtrl = function ($scope, $location, powerUnitService) {
        $scope.powerUnits = {};
        powerUnitService.powerUnits().then(function (eq) {
            $scope.powerUnits = eq;
        });

        $scope.createPowerUnit = function () {
            $location.path('/powerunits_create');
        };


        $scope.show = function (powerUnit) {
            powerUnitService.powerUnit(powerUnit.id).then(function (eq) {
                $location.path('/powerunits/' + eq.id);
            });
        };


    };
    ShowPowerUnitsCtrl.$inject = ['$scope', '$location', 'powerUnitService'];


    return {
        CreatePowerUnitCtrl: CreatePowerUnitCtrl,
        ShowPowerUnitsCtrl: ShowPowerUnitsCtrl,
        ShowPowerUnitCtrl: ShowPowerUnitCtrl
    };

});
