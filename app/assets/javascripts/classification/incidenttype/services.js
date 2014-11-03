define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('incidenttype.services', ['prm.common', 'ngCookies']);
  mod.factory('incidenttypeService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        incidentTypes: function () {
            return playRoutes.controllers.IncidentTypes.incidentTypes().get().then(function (response) {
                return response.data;
            });
        },
        incidentType: function (id) {
        return playRoutes.controllers.IncidentTypes.incidentType(id).get().then(function (response) {
          return response.data;
        });
      },
      createIncidentType: function (data) {
        return playRoutes.controllers.IncidentTypes.createIncidentType().post(data).then(function (response) {
          $log.info("Created new incidenttype");
            return response.data;
        });
      },
      updateIncidentType: function (data) {
            return playRoutes.controllers.IncidentTypes.updateIncidentType().post(data).then(function () {
                $log.info("Update incidenttype");
            });
        },
       deleteIncidentType: function (id) {
            return playRoutes.controllers.IncidentTypes.deleteIncidentType(id).delete().then(function () {
                $log.info("deleted incidenttype");
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
