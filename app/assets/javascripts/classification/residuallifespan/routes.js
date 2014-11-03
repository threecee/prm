/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('residuallifespan.routes', ['residuallifespan.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/residuallifespans', {templateUrl:'/assets/javascripts/classification/residuallifespan/residuallifespans.html', controller:controllers.ShowResidualLifeSpansCtrl})
      .when('/residuallifespans/:id', {templateUrl:'/assets/javascripts/classification/residuallifespan/residuallifespan.html', controller:controllers.ShowResidualLifeSpanCtrl})
      .when('/residuallifespan_create', {templateUrl:'/assets/javascripts/classification/residuallifespan/residuallifespan_create.html', controller:controllers.CreateResidualLifeSpanCtrl});
  }]);
  return mod;
});
