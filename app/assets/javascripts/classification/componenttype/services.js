define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('componenttype.services', ['prm.common', 'ngCookies']);
  mod.factory('componentTypeService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        componentTypes: function () {
            return playRoutes.controllers.ComponentTypes.componentTypes().get().then(function (response) {
                return response.data;
            });
        },
        componentType: function (id) {
        return playRoutes.controllers.ComponentTypes.componentType(id).get().then(function (response) {
          return response.data;
        });
      },
      createComponentType: function (data) {
        return playRoutes.controllers.ComponentTypes.createComponentType().post(data).then(function (response) {
          $log.info("Created new componenttype");
            return response.data;
        });
      },
      updateComponentType: function (data) {
            return playRoutes.controllers.ComponentTypes.updateComponentType().post(data).then(function () {
                $log.info("Update componenttype");
            });
        },
       deleteComponentType: function (id) {
            return playRoutes.controllers.ComponentTypes.deleteComponentType(id).delete().then(function () {
                $log.info("deleted componenttype");
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
