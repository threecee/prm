/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('componenttype.routes', ['componenttype.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/componenttypes', {templateUrl:'/assets/javascripts/classification/componenttype/componenttypes.html', controller:controllers.ShowComponentTypesCtrl})
      .when('/componenttypes/:id', {templateUrl:'/assets/javascripts/classification/componenttype/componenttype.html', controller:controllers.ShowComponentTypeCtrl})
      .when('/componenttype_create', {templateUrl:'/assets/javascripts/classification/componenttype/componenttype_create.html', controller:controllers.CreateComponentTypeCtrl});
  }]);
  return mod;
});
