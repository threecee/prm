define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('equipmentstate.services', ['prm.common', 'ngCookies']);
  mod.factory('equipmentstateService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        equipmentStates: function () {
            return playRoutes.controllers.EquipmentStates.equipmentStates().get().then(function (response) {
                return response.data;
            });
        },
        equipmentState: function (id) {
        return playRoutes.controllers.EquipmentStates.equipmentState(id).get().then(function (response) {
          return response.data;
        });
      },
      createEquipmentState: function (data) {
        return playRoutes.controllers.EquipmentStates.createEquipmentState().post(data).then(function (response) {
          $log.info("Created new equipmentstate");
            return response.data;
        });
      },
      updateEquipmentState: function (data) {
            return playRoutes.controllers.EquipmentStates.updateEquipmentState().post(data).then(function () {
                $log.info("Update equipmentstate");
            });
        },
       deleteEquipmentState: function (id) {
            return playRoutes.controllers.EquipmentStates.deleteEquipmentState(id).delete().then(function () {
                $log.info("deleted equipmentstate");
            });
        }
    };
  }]);

  /**
   * If the current route does not resolve, go back to the start page.
   */
  var handleRouteError = function ($rootScope, $location) {
    $rootScope.$on('$routeChangeError', function (/*e, next, current*/) {
      $location.path('/');
    });
  };
  handleRouteError.$inject = ['$rootScope', '$location'];
  mod.run(handleRouteError);
  return mod;
});
