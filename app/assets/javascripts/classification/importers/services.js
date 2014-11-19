define(['angular', 'common'], function (angular) {
  'use strict';

  var mod = angular.module('importers.services', ['prm.common', 'ngCookies']);
  mod.factory('importersService', [ function () {

    return {

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
