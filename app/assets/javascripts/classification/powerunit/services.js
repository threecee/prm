define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('powerunit.services', ['prm.common', 'ngCookies']);
  mod.factory('powerUnitService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        powerUnits: function () {
            return playRoutes.controllers.PowerUnits.powerUnits().get().then(function (response) {
                return response.data;
            });
        },
        powerUnit: function (id) {
        return playRoutes.controllers.PowerUnits.powerUnit(id).get().then(function (response) {
          return response.data;
        });
      },
      createPowerUnit: function (powerStationId, data) {
        return playRoutes.controllers.PowerUnits.createPowerUnit(powerStationId).post(data).then(function (response) {
          $log.info("Created new powerunit");
            return response.data;
        });
      },
      updatePowerUnit: function (powerStationId, data) {
            return playRoutes.controllers.PowerUnits.updatePowerUnit(powerStationId).post(data).then(function () {
                $log.info("Update powerunit");
            });
        },
       deletePowerUnit: function (id) {
            return playRoutes.controllers.PowerUnits.deletePowerUnit(id).delete().then(function () {
                $log.info("deleted powerunit");
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
