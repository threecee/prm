define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('downtimecost.services', ['prm.common', 'ngCookies']);
  mod.factory('downtimecostService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        downtimeCosts: function () {
            return playRoutes.controllers.DowntimeCosts.downtimeCosts().get().then(function (response) {
                return response.data;
            });
        },
        downtimeCost: function (id) {
        return playRoutes.controllers.DowntimeCosts.downtimeCost(id).get().then(function (response) {
          return response.data;
        });
      },
      createDowntimeCostForPowerStation: function (powerStationId, data) {
        return playRoutes.controllers.DowntimeCosts.createDowntimeCostForPowerStation(powerStationId).post(data).then(function (response) {
          $log.info("Created new downtimecost");
            return response.data;
        });
      },
      updateDowntimeCostForPowerStation: function (powerStationId, data) {
            return playRoutes.controllers.DowntimeCosts.updateDowntimeCostForPowerStation(powerStationId).post(data).then(function () {
                $log.info("Update downtimecost");
            });
        },
        createDowntimeCostForPowerUnit: function (powerUnitId, data) {
            return playRoutes.controllers.DowntimeCosts.createDowntimeCostForPowerUnit(powerUnitId).post(data).then(function (response) {
                $log.info("Created new downtimecost");
                return response.data;
            });
        },
        updateDowntimeCostForPowerUnit: function (powerUnitId, data) {
            return playRoutes.controllers.DowntimeCosts.updateDowntimeCostForPowerUnit(powerUnitId).post(data).then(function () {
                $log.info("Update downtimecost");
            });
        },
       deleteDowntimeCost: function (id) {
            return playRoutes.controllers.DowntimeCosts.deleteDowntimeCost(id).delete().then(function () {
                $log.info("deleted downtimecost");
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
