/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('incidenttype.routes', ['incidenttype.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/incidenttypes', {templateUrl:'/assets/javascripts/classification/incidenttype/incidenttypes.html', controller:controllers.ShowIncidentTypesCtrl})
      .when('/incidenttypes/:id', {templateUrl:'/assets/javascripts/classification/incidenttype/incidenttype.html', controller:controllers.ShowIncidentTypeCtrl})
      .when('/incidenttype_create', {templateUrl:'/assets/javascripts/classification/incidenttype/incidenttype_create.html', controller:controllers.CreateIncidentTypeCtrl});
  }]);
  return mod;
});
