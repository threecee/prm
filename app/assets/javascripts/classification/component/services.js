define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('component.services', ['prm.common', 'ngCookies']);
  mod.factory('componentService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        components: function () {
            return playRoutes.controllers.Components.components().get().then(function (response) {
                return response.data;
            });
        },
        component: function (id) {
        return playRoutes.controllers.Components.component(id).get().then(function (response) {
          return response.data;
        });
      },
      createComponentForPowerStation: function (id, data) {
        return playRoutes.controllers.Components.createComponentForPowerStation(id).post(data).then(function (response) {
          $log.info("Created new component");
            return response.data;
        });
      },
        createComponentForPowerUnit: function (id, data) {
            return playRoutes.controllers.Components.createComponent(id).post(data).then(function (response) {
                $log.info("Created new component");
                return response.data;
            });
        },
        updateComponentForPowerStation: function (id, data) {
            return playRoutes.controllers.Components.updateComponentForPowerStation(id).post(data).then(function () {
                $log.info("Update component");
            });
        },
        updateComponentForPowerUnit: function (id, data) {
            return playRoutes.controllers.Components.updateComponentForPowerUnit(id).post(data).then(function () {
                $log.info("Update component");
            });
        },
        updateComponentForPowerStationByValues: function (idStation, idType, data) {
            return playRoutes.controllers.Components.updateComponentForPowerStation(idStation, idType).post(data).then(function () {
                $log.info("Update component");
            });
        },
        updateComponentForPowerUnitByValues: function (idUnit, idType, data) {
            return playRoutes.controllers.Components.updateComponentForPowerUnit(idUnit, idType).post(data).then(function () {
                $log.info("Update component");
            });
        },
       deleteComponent: function (id) {
            return playRoutes.controllers.Components.deleteComponent(id).delete().then(function () {
                $log.info("deleted component");
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
