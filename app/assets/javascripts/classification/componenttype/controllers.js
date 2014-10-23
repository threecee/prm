define([], function() {
    'use strict';

    var CreateComponentTypeCtrl = function($scope, $location, componenttypeService) {

        $scope.createComponentType = function(componentType) {
            componenttypeService.createComponentType(componentType).then(function(eq) {
                $location.path('/componenttypes/' + eq.id );
            });
        };

        $scope.getComponentType = function(id) {
            componenttypeService.componentType(id).then(function(eq) {
                return eq;
            });
        };

    };
    CreateComponentTypeCtrl.$inject = ['$scope', '$location', 'componenttypeService'];

    var ShowComponentTypeCtrl = function($scope, $routeParams, $location, componenttypeService, $log) {
        $scope.componentType = {};
        componenttypeService.componentType($routeParams.id).then(function(eq) {
            $scope.componentType = eq;
            $log.debug("ComponentType value: " + $scope.componentType.value);
        });
    };
    ShowComponentTypeCtrl.$inject = ['$scope', '$routeParams', '$location', 'componenttypeService', '$log'];

    var ShowComponentTypesCtrl = function($scope,  $location, componenttypeService) {
        $scope.componentTypes = {};
        componenttypeService.componentTypes().then(function(eq) {
            $scope.componentTypes = eq;
        });


        $scope.delete = function(componentType, index) {
            componenttypeService.deleteComponentType(componentType.id).then(function() {
                $scope.componentTypes.splice(index,1);
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
