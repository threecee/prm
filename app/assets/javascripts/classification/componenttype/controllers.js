define(['./componenttype_edit_directive'], function () {
    'use strict';

    var CreateComponentTypeCtrl = function ($scope, $location, componentTypeService) {

        $scope.createComponentType = function (componentType) {
            componentType.residuallifespans = [];
            componentType.repairs = [];

            componentTypeService.createComponentType(componentType).then(function (eq) {
                $location.path('/componenttypes/' + eq.id);
            });
        };

        $scope.getComponentType = function (id) {
            componentTypeService.componentType(id).then(function (eq) {
                return eq;
            });
        };

    };
    CreateComponentTypeCtrl.$inject = ['$scope', '$location', 'componentTypeService'];

    var ShowComponentTypeCtrl = function ($scope, $routeParams, $q, $location, componentTypeService, $log, equipmentstateService, residuallifespanService, incidenttypeService, repairService) {
        var deferred = $q.defer();
        var promise = deferred.promise;

        $scope.componentType = {};
        $scope.equipmentStates = {};
        $scope.incidentTypes = {};



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
            componentTypeService.componentType($routeParams.id).then(function (eq) {
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
            componentTypeService.deleteComponentType(componentType.id).then(function () {
                $scope.componentTypes.splice(index, 1);
            });
        };
        $scope.update = function (componentType) {
            componentTypeService.updateComponentType(componentType.id).then(function () {
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
        $scope.updateRepair = function (newobject, incidentType) {
            if (incidentType.repair !== null) {
                repairService.updateRepair($scope.componentType.id, incidentType.repair);
            }
            else {
                repairService.createRepairByValues($scope.componentType.id, incidentType.id, newobject.span, newobject.cost, newobject.probability).then(function (newrepair) {
                    $scope.componentType.repairs.push(newrepair);
                    incidentType.repair = newrepair;
                });
            }
            return false;
        };


        $scope.getRepair = function (incidentType) {
            var index = $scope.getResidualLifeSpanIndex(incidentType);
            if (index !== null) {
                $log.debug("found repair for incidentType " + incidentType.value + ": " + $scope.componentType.repairs[index].span);
                return $scope.componentType.repairs[index];
            }
            return null;
        };

        $scope.getRepairIndex = function (incidentType) {
            for (var i = 0; i < $scope.componentType.repairs.length; i++) {
                if ($scope.componentType.repairs[i].incidentType.value === incidentType.value) {
                    return i;
                }
            }
            return null;

        };



        $scope.getIncidentTypes = function () {
            incidenttypeService.incidentTypes().then(function (it) {
                $scope.incidentTypes = it;
                promise.then(function () {
                    $log.debug("Promise trigget");
                    for (var i = 0; i < $scope.incidentTypes.length; i++) {
                        $scope.incidentTypes[i].repair = $scope.getRepair($scope.incidentTypes[i]);
                        if ($scope.incidentTypes[i].repair !== null) {
                            $log.debug("value for incidenttype " + $scope.incidentTypes[i].value + ": " + $scope.incidentTypes[i].repair.span);
                        }
                    }
                });
            });
        };



        $scope.getEquipmentStates();
        $scope.getIncidentTypes();
        $scope.getComponentType();

    };
    ShowComponentTypeCtrl.$inject = ['$scope', '$routeParams', '$q', '$location', 'componentTypeService', '$log', 'equipmentstateService', 'residuallifespanService', 'incidenttypeService', 'repairService'];

    var ShowComponentTypesCtrl = function ($scope, $location, componentTypeService) {
        $scope.componentTypes = {};
        componentTypeService.componentTypes().then(function (eq) {
            $scope.componentTypes = eq;
        });

        $scope.createComponentType = function () {
            $location.path('/componenttypes_create');
        };


        $scope.show = function (componentType) {
            componentTypeService.componentType(componentType.id).then(function (eq) {
                $location.path('/componenttypes/' + eq.id);
            });
        };


    };
    ShowComponentTypesCtrl.$inject = ['$scope', '$location', 'componentTypeService'];


    return {
        CreateComponentTypeCtrl: CreateComponentTypeCtrl,
        ShowComponentTypesCtrl: ShowComponentTypesCtrl,
        ShowComponentTypeCtrl: ShowComponentTypeCtrl
    };

});
