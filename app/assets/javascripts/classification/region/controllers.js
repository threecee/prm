define([], function() {
  'use strict';

  var CreateRegionCtrl = function($scope, $location, regionService) {

      $scope.createRegion = function(region) {
          regionService.createRegion(region).then(function(eq) {
              $location.path('/regions/' + eq.id );
          });
      };

      $scope.getRegion = function(id) {
          regionService.region(id).then(function(eq) {
             return eq;
          });
      };

  };
    CreateRegionCtrl.$inject = ['$scope', '$location', 'regionService'];

    var ShowRegionCtrl = function($scope, $routeParams, $location, regionService, $log) {
        $scope.region = {};
        regionService.region($routeParams.id).then(function(eq) {
             $scope.region = eq;
             $log.debug("Region value: " + $scope.region.value);
        });
    };
    ShowRegionCtrl.$inject = ['$scope', '$routeParams', '$location', 'regionService', '$log'];

    var ShowRegionsCtrl = function($scope,  $location, regionService) {
        $scope.regions = {};
        regionService.regions().then(function(eq) {
            $scope.regions = eq;
        });


        $scope.delete = function(region, index) {
            regionService.deleteRegion(region.id).then(function() {
                $scope.regions.splice(index,1);
            });
        };
        $scope.update = function(region) {
            regionService.updateRegion(region.id).then(function() {

            });
        };


    };
    ShowRegionsCtrl.$inject = ['$scope', '$location', 'regionService'];


    return {
        CreateRegionCtrl: CreateRegionCtrl,
        ShowRegionsCtrl: ShowRegionsCtrl,
      ShowRegionCtrl: ShowRegionCtrl
  };

});
