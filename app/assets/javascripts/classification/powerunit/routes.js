/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('powerunit.routes', ['powerunit.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/powerunits', {templateUrl:'/assets/javascripts/classification/powerunit/powerunits.html', controller:controllers.ShowPowerUnitsCtrl})
      .when('/powerunits/:id', {templateUrl:'/assets/javascripts/classification/powerunit/powerunit.html', controller:controllers.ShowPowerUnitCtrl})
      .when('/powerunit_create', {templateUrl:'/assets/javascripts/classification/powerunit/powerunit_create.html', controller:controllers.CreatePowerUnitCtrl});
  }]);
  return mod;
});
