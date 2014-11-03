define([], function() {
  'use strict';

  var CreateEquipmentStateCtrl = function($scope, $location, equipmentstateService) {

      $scope.createEquipmentState = function(equipmentState) {
          equipmentstateService.createEquipmentState(equipmentState).then(function(eq) {
              $location.path('/equipmentstates/' + eq.id );
          });
      };

      $scope.getEquipmentState = function(id) {
          equipmentstateService.equipmentState(id).then(function(eq) {
             return eq;
          });
      };

  };
    CreateEquipmentStateCtrl.$inject = ['$scope', '$location', 'equipmentstateService'];

    var ShowEquipmentStateCtrl = function($scope, $routeParams, $location, equipmentstateService, $log) {
        $scope.equipmentState = {};
        equipmentstateService.equipmentState($routeParams.id).then(function(eq) {
             $scope.equipmentState = eq;
             $log.debug("EquipmentState value: " + $scope.equipmentState.value);
        });
    };
    ShowEquipmentStateCtrl.$inject = ['$scope', '$routeParams', '$location', 'equipmentstateService', '$log'];

    var ShowEquipmentStatesCtrl = function($scope,  $location, equipmentstateService) {
        $scope.equipmentStates = {};
        equipmentstateService.equipmentStates().then(function(eq) {
            $scope.equipmentStates = eq;
        });


        $scope.delete = function(equipmentState, index) {
            equipmentstateService.deleteEquipmentState(equipmentState.id).then(function() {
                $scope.equipmentStates.splice(index,1);
            });
        };
        $scope.update = function(equipmentState) {
            equipmentstateService.updateEquipmentState(equipmentState.id).then(function() {

            });
        };


    };
    ShowEquipmentStatesCtrl.$inject = ['$scope', '$location', 'equipmentstateService'];


    return {
        CreateEquipmentStateCtrl: CreateEquipmentStateCtrl,
        ShowEquipmentStatesCtrl: ShowEquipmentStatesCtrl,
      ShowEquipmentStateCtrl: ShowEquipmentStateCtrl
  };

});
