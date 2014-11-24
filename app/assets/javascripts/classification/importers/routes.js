define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('importers.routes', ['importers.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/importers', {templateUrl:'/assets/javascripts/classification/importers/importers.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations', {templateUrl:'/assets/javascripts/classification/importers/powerstations.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits', {templateUrl:'/assets/javascripts/classification/importers/powerunits.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_components', {templateUrl:'/assets/javascripts/classification/importers/powerstations_components.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_components', {templateUrl:'/assets/javascripts/classification/importers/powerunits_components.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_componenttypes', {templateUrl:'/assets/javascripts/classification/importers/powerstations_componenttypes.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_componenttypes', {templateUrl:'/assets/javascripts/classification/importers/powerunits_componenttypes.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_components_states', {templateUrl:'/assets/javascripts/classification/importers/powerstations_components_states.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_components_states', {templateUrl:'/assets/javascripts/classification/importers/powerunits_components_states.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_components_repairs', {templateUrl:'/assets/javascripts/classification/importers/powerstations_components_repairs.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_components_repairs', {templateUrl:'/assets/javascripts/classification/importers/powerunits_components_repairs.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_components_unplanned_unavailability_costs', {templateUrl:'/assets/javascripts/classification/importers/powerstations_components_unplanned_unavailability_costs.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_components_unplanned_unavailability_costs', {templateUrl:'/assets/javascripts/classification/importers/powerunits_components_unplanned_unavailability_costs.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerstations_components_planned_unavailability_costs', {templateUrl:'/assets/javascripts/classification/importers/powerstations_components_planned_unavailability_costs.html', controller:controllers.ShowImportersCtrl})
        .when('/importers/powerunits_components_planned_unavailability_costs', {templateUrl:'/assets/javascripts/classification/importers/powerunits_components_planned_unavailability_costs.html', controller:controllers.ShowImportersCtrl})
      .when('/importers/general_lifespans', {templateUrl:'/assets/javascripts/classification/importers/general_lifespans.html', controller:controllers.ShowImportersCtrl});
  }]);
  return mod;
});


