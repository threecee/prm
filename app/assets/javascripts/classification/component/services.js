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
      createComponent: function (data) {
        return playRoutes.controllers.Components.createComponent().post(data).then(function (response) {
          $log.info("Created new component");
            return response.data;
        });
      },
      updateComponent: function (data) {
            return playRoutes.controllers.Components.updateComponent().post(data).then(function () {
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
