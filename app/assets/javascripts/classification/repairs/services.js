define(['angular', 'common'], function (angular) {
    'use strict';

    var mod = angular.module('repair.services', ['prm.common', 'ngCookies']);
    mod.factory('repairService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {

        return {
            repairs: function () {
                return playRoutes.controllers.Repairs.repairs().get().then(function (response) {
                    return response.data;
                });
            },
            repair: function (id) {
                return playRoutes.controllers.Repairs.repair(id).get().then(function (response) {
                    return response.data;
                });
            },

            createRepairByValues: function (componentId, equipmentStateId, span, cost, probability) {
                return playRoutes.controllers.Repairs.createRepairByValues(componentId, equipmentStateId, span, cost, probability).post().then(function (response) {
                    $log.info("Created new repair");
                    return response.data;
                });
            },

            createRepair: function (componentId, data) {
                return playRoutes.controllers.Repairs.createRepair(componentId).post(data).then(function (response) {
                    $log.info("Created new repair");
                    return response.data;
                });
            },
            updateRepair: function (data) {
                return playRoutes.controllers.Repairs.updateRepair().post(data).then(function () {
                    $log.info("Update repair");
                });
            },
            deleteRepair: function (id) {
                return playRoutes.controllers.Repairs.deleteRepair(id).delete().then(function () {
                    $log.info("deleted repair");
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
