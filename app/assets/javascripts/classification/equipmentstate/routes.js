/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('equipmentstate.routes', ['equipmentstate.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/equipmentstates', {templateUrl:'/assets/javascripts/classification/equipmentstate/equipmentstates.html', controller:controllers.ShowEquipmentStatesCtrl})
      .when('/equipmentstates/:id', {templateUrl:'/assets/javascripts/classification/equipmentstate/equipmentstate.html', controller:controllers.ShowEquipmentStateCtrl})
      .when('/equipmentstate_create', {templateUrl:'/assets/javascripts/classification/equipmentstate/equipmentstate_create.html', controller:controllers.CreateEquipmentStateCtrl});
  }]);
  return mod;
});
