define(['angular'], function(angular) {
  'use strict';

  var mod = angular.module('prm.classification.componenttypes.edit', []);
  mod.directive('editComponentType', ['$log', function($log) {
    return {
      restrict: 'AE',
      link: function(/*scope, el, attrs*/) {
        $log.info('Here prints the example directive from /common/directives.');
      }
    };
  }]);
  return mod;
});
