define([], function() {
    'use strict';

    var CreateResidualLifeSpanCtrl = function($scope, $location, residuallifespanService, componentTypeService, equipmentstateService) {
        $scope.componentTypes = {};
        $scope.equipmentStates = {};
        equipmentstateService.equipmentStates().then(function(eq) {
            $scope.equipmentStates = eq;
        });
        componentTypeService.componentTypes().then(function(eq) {
            $scope.componentTypes = eq;
        });

        $scope.createResidualLifeSpan = function(residualLifeSpan) {
            residuallifespanService.createResidualLifeSpan(residualLifeSpan).then(function(eq) {
                $location.path('/residuallifespans/' + eq.id );
            });
        };

        $scope.getResidualLifeSpan = function(id) {
            residuallifespanService.residualLifeSpan(id).then(function(eq) {
                return eq;
            });
        };

    };
    CreateResidualLifeSpanCtrl.$inject = ['$scope', '$location', 'residuallifespanService', 'componentTypeService', 'equipmentstateService'];

    var ShowResidualLifeSpanCtrl = function($scope, $routeParams, $location, residuallifespanService, $log) {
        $scope.residualLifeSpan = {};
        residuallifespanService.residualLifeSpan($routeParams.id).then(function(eq) {
            $scope.residualLifeSpan = eq;
            $log.debug("ResidualLifeSpan value: " + $scope.residualLifeSpan.value);
        });
    };
    ShowResidualLifeSpanCtrl.$inject = ['$scope', '$routeParams', '$location', 'residuallifespanService', '$log'];

    var ShowResidualLifeSpansCtrl = function($scope,  $location, residuallifespanService) {
        $scope.residualLifeSpans = {};
        residuallifespanService.residualLifeSpans().then(function(eq) {
            $scope.residualLifeSpans = eq;
        });


        $scope.delete = function(residualLifeSpan, index) {
            residuallifespanService.deleteResidualLifeSpan(residualLifeSpan.id).then(function() {
                $scope.residualLifeSpans.splice(index,1);
            });
        };


    };
    ShowResidualLifeSpansCtrl.$inject = ['$scope', '$location', 'residuallifespanService'];


    return {
        CreateResidualLifeSpanCtrl: CreateResidualLifeSpanCtrl,
        ShowResidualLifeSpansCtrl: ShowResidualLifeSpansCtrl,
        ShowResidualLifeSpanCtrl: ShowResidualLifeSpanCtrl
    };

});
