define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('region.routes', ['region.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/regions', {templateUrl:'/assets/javascripts/classification/region/regions.html', controller:controllers.ShowRegionsCtrl})
      .when('/regions/:id', {templateUrl:'/assets/javascripts/classification/region/region.html', controller:controllers.ShowRegionCtrl})
      .when('/region_create', {templateUrl:'/assets/javascripts/classification/region/region_create.html', controller:controllers.CreateRegionCtrl});
  }]);
  return mod;
});
