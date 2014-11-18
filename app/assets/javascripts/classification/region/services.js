define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('region.services', ['prm.common', 'ngCookies']);
  mod.factory('regionService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        regions: function () {
            return playRoutes.controllers.Regions.regions().get().then(function (response) {
                return response.data;
            });
        },
        region: function (id) {
        return playRoutes.controllers.Regions.region(id).get().then(function (response) {
          return response.data;
        });
      },
      createRegion: function (data) {
        return playRoutes.controllers.Regions.createRegion().post(data).then(function (response) {
          $log.info("Created new region");
            return response.data;
        });
      },
      updateRegion: function (data) {
            return playRoutes.controllers.Regions.updateRegion().post(data).then(function () {
                $log.info("Update region");
            });
        },
       deleteRegion: function (id) {
            return playRoutes.controllers.Regions.deleteRegion(id).delete().then(function () {
                $log.info("deleted region");
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
