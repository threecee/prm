define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('group.routes', ['group.services', 'prm.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/groups', {templateUrl:'/assets/javascripts/classification/group/groups.html', controller:controllers.ShowGroupsCtrl})
      .when('/groups/:id', {templateUrl:'/assets/javascripts/classification/group/group.html', controller:controllers.ShowGroupCtrl})
      .when('/group_create', {templateUrl:'/assets/javascripts/classification/group/group_create.html', controller:controllers.CreateGroupCtrl});
  }]);
  return mod;
});
