define(['./componenttype_edit_directive'], function () {
    'use strict';

    var CreateComponentTypeCtrl = function ($scope, $location, componenttypeService) {

        $scope.createComponentType = function (componentType) {
            componenttypeService.createComponentType(componentType).then(function (eq) {
                $location.path('/componenttypes/' + eq.id);
            });
        };

        $scope.getComponentType = function (id) {
            componenttypeService.componentType(id).then(function (eq) {
                return eq;
            });
        };

    };
    CreateComponentTypeCtrl.$inject = ['$scope', '$location', 'componenttypeService'];

    var ShowComponentTypeCtrl = function ($scope, $routeParams, $q, $location, componenttypeService, $log, equipmentstateService, residuallifespanService) {
        var deferred = $q.defer();
        var promise = deferred.promise;

        $scope.componentType = {};
        $scope.equipmentStates = {};



        $scope.getEquipmentStates = function () {
            equipmentstateService.equipmentStates().then(function (eq) {
                $scope.equipmentStates = eq;
                promise.then(function () {
                    for (var i = 0; i < $scope.equipmentStates.length; i++) {
                        $scope.equipmentStates[i].residuallifespan = $scope.getResidualLifeSpan($scope.equipmentStates[i]);
                        if ($scope.equipmentStates[i].residuallifespan !== null) {
                            $log.debug("value for equipmentstate " + $scope.equipmentStates[i].value + ": " + $scope.equipmentStates[i].residuallifespan.span);
                        }
                    }
                });
            });
        };

        $scope.getComponentType = function () {
            componenttypeService.componentType($routeParams.id).then(function (eq) {
                $scope.componentType = eq;
                deferred.resolve();
                $log.debug("ComponentType value: " + $scope.componentType.name);
            });
        };

        $scope.getResidualLifeSpan = function (equipmentState) {
            var index = $scope.getResidualLifeSpanIndex(equipmentState);
            if (index !== null) {
                $log.debug("found residuallifespan for equipmentstate " + equipmentState.value + ": " + $scope.componentType.residuallifespans[index].span);
                return $scope.componentType.residuallifespans[index];
            }
            return null;
        };

        $scope.getResidualLifeSpanIndex = function (equipmentState) {
            for (var i = 0; i < $scope.componentType.residuallifespans.length; i++) {
                if ($scope.componentType.residuallifespans[i].equipmentState.value === equipmentState.value) {
                    return i;
                }
            }
            return null;

        };


        $scope.delete = function (componentType, index) {
            componenttypeService.deleteComponentType(componentType.id).then(function () {
                $scope.componentTypes.splice(index, 1);
            });
        };
        $scope.update = function (componentType) {
            componenttypeService.updateComponentType(componentType.id).then(function () {
            });
        };

        $scope.updateResidualLifeSpan = function (newspan, equipmentState) {
            if (equipmentState.residuallifespan !== null) {
                residuallifespanService.updateResidualLifeSpan($scope.componentType.id, equipmentState.residuallifespan);
            }
            else {
                residuallifespanService.createResidualLifeSpanByValues($scope.componentType.id, equipmentState.id, newspan).then(function (newresiduallifespan) {
                    $scope.componentType.residuallifespans.push(newresiduallifespan);
                    equipmentState.residuallifespan = newresiduallifespan;
                });
            }
            return false;
        };

        $scope.getComponentType();
        $scope.getEquipmentStates();


    };
    ShowComponentTypeCtrl.$inject = ['$scope', '$routeParams', '$q', '$location', 'componenttypeService', '$log', 'equipmentstateService', 'residuallifespanService'];

    var ShowComponentTypesCtrl = function ($scope, $location, componenttypeService) {
        $scope.componentTypes = {};
        componenttypeService.componentTypes().then(function (eq) {
            $scope.componentTypes = eq;
        });


        $scope.show = function (componentType) {
            componenttypeService.componentType(componentType.id).then(function (eq) {
                $location.path('/componenttypes/' + eq.id);
            });
        };


    };
    ShowComponentTypesCtrl.$inject = ['$scope', '$location', 'componenttypeService'];


    return {
        CreateComponentTypeCtrl: CreateComponentTypeCtrl,
        ShowComponentTypesCtrl: ShowComponentTypesCtrl,
        ShowComponentTypeCtrl: ShowComponentTypeCtrl
    };

});
