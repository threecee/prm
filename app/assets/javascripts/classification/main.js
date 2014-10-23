/**
 * User package module.
 * Manages all sub-modules so other RequireJS modules only have to import the package.
 */
define(['angular',
    './componenttype/routes', './componenttype/services',
    './equipmentstate/routes', './equipmentstate/services',
    './incidenttype/routes', './incidenttype/services'
], function(angular) {
  'use strict';

  return angular.module('prm.classification', ['ngCookies', 'ngRoute',
      'componenttype.routes', 'componenttype.services',
      'equipmentstate.routes', 'equipmentstate.services',
      'incidenttype.routes', 'incidenttype.services'
  ]);
});
