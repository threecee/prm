define([], function() {
  'use strict';

  var CreateGroupCtrl = function($scope, $location, groupService) {

      $scope.createGroup = function(group) {
          groupService.createGroup(group).then(function(eq) {
              $location.path('/groups/' + eq.id );
          });
      };

      $scope.getGroup = function(id) {
          groupService.group(id).then(function(eq) {
             return eq;
          });
      };

  };
    CreateGroupCtrl.$inject = ['$scope', '$location', 'groupService'];

    var ShowGroupCtrl = function($scope, $routeParams, $location, groupService, $log) {
        $scope.group = {};
        groupService.group($routeParams.id).then(function(eq) {
             $scope.group = eq;
             $log.debug("Group value: " + $scope.group.value);
        });
    };
    ShowGroupCtrl.$inject = ['$scope', '$routeParams', '$location', 'groupService', '$log'];

    var ShowGroupsCtrl = function($scope,  $location, groupService) {
        $scope.groups = {};
        groupService.groups().then(function(eq) {
            $scope.groups = eq;
        });


        $scope.delete = function(group, index) {
            groupService.deleteGroup(group.id).then(function() {
                $scope.groups.splice(index,1);
            });
        };
        $scope.update = function(group) {
            groupService.updateGroup(group.id).then(function() {

            });
        };


    };
    ShowGroupsCtrl.$inject = ['$scope', '$location', 'groupService'];


    return {
        CreateGroupCtrl: CreateGroupCtrl,
        ShowGroupsCtrl: ShowGroupsCtrl,
      ShowGroupCtrl: ShowGroupCtrl
  };

});
