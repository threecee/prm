define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('powerstation.services', ['prm.common', 'ngCookies']);
  mod.factory('powerStationService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        powerStations: function () {
            return playRoutes.controllers.PowerStations.powerStations().get().then(function (response) {
                return response.data;
            });
        },
        powerStation: function (id) {
        return playRoutes.controllers.PowerStations.powerStation(id).get().then(function (response) {
          return response.data;
        });
      },
      createPowerStation: function (data) {
        return playRoutes.controllers.PowerStations.createPowerStation().post(data).then(function (response) {
          $log.info("Created new powerstation");
            return response.data;
        });
      },
      updatePowerStation: function (data) {
            return playRoutes.controllers.PowerStations.updatePowerStation().post(data).then(function () {
                $log.info("Update powerstation");
            });
        },
       deletePowerStation: function (id) {
            return playRoutes.controllers.PowerStations.deletePowerStation(id).delete().then(function () {
                $log.info("deleted powerstation");
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
