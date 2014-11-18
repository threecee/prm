define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('group.services', ['prm.common', 'ngCookies']);
  mod.factory('groupService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

    return {
        groups: function () {
            return playRoutes.controllers.Groups.groups().get().then(function (response) {
                return response.data;
            });
        },
        group: function (id) {
        return playRoutes.controllers.Groups.group(id).get().then(function (response) {
          return response.data;
        });
      },
      createGroup: function (data) {
        return playRoutes.controllers.Groups.createGroup().post(data).then(function (response) {
          $log.info("Created new group");
            return response.data;
        });
      },
      updateGroup: function (data) {
            return playRoutes.controllers.Groups.updateGroup().post(data).then(function () {
                $log.info("Update group");
            });
        },
       deleteGroup: function (id) {
            return playRoutes.controllers.Groups.deleteGroup(id).delete().then(function () {
                $log.info("deleted group");
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
