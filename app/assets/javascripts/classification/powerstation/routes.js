/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('powerstation.routes', ['powerstation.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/powerstations', {templateUrl:'/assets/javascripts/classification/powerstation/powerstations.html', controller:controllers.ShowPowerStationsCtrl})
      .when('/powerstations/:id', {templateUrl:'/assets/javascripts/classification/powerstation/powerstation.html', controller:controllers.ShowPowerStationCtrl})
      .when('/powerstation_create', {templateUrl:'/assets/javascripts/classification/powerstation/powerstation_create.html', controller:controllers.CreatePowerStationCtrl});
  }]);
  return mod;
});
