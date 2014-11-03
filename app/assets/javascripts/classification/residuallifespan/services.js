define(['angular', 'common'], function (angular) {
    'use strict';

    var mod = angular.module('residuallifespan.services', ['prm.common', 'ngCookies']);
    mod.factory('residuallifespanService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

        return {
            residualLifeSpans: function () {
                return playRoutes.controllers.ResidualLifeSpans.residualLifeSpans().get().then(function (response) {
                    return response.data;
                });
            },
            residualLifeSpan: function (id) {
                return playRoutes.controllers.ResidualLifeSpans.residualLifeSpan(id).get().then(function (response) {
                    return response.data;
                });
            },

            createResidualLifeSpanByValues: function (componentId, equipmentStateId, span) {
                return playRoutes.controllers.ResidualLifeSpans.createResidualLifeSpanByValues(componentId, equipmentStateId, span).post().then(function (response) {
                    $log.info("Created new residuallifespan");
                    return response.data;
                });
            },

            createResidualLifeSpan: function (componentId, data) {
                return playRoutes.controllers.ResidualLifeSpans.createResidualLifeSpan(componentId).post(data).then(function (response) {
                    $log.info("Created new residuallifespan");
                    return response.data;
                });
            },
            updateResidualLifeSpan: function (data) {
                return playRoutes.controllers.ResidualLifeSpans.updateResidualLifeSpan().post(data).then(function () {
                    $log.info("Update residuallifespan");
                });
            },
            deleteResidualLifeSpan: function (id) {
                return playRoutes.controllers.ResidualLifeSpans.deleteResidualLifeSpan(id).delete().then(function () {
                    $log.info("deleted residuallifespan");
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
