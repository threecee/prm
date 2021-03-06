/**
 * User package module.
 * Manages all sub-modules so other RequireJS modules only have to import the package.
 */
define(['angular',
    './componenttype/routes', './componenttype/services',
    './equipmentstate/routes', './equipmentstate/services',
    './incidenttype/routes', './incidenttype/services',
    './residuallifespan/routes', './residuallifespan/services', './residuallifespan/edit_directive',
    './repairs/services',
    './downtimecost/services',
    './powerunit/routes', './powerunit/services',
    './powerstation/routes', './powerstation/services',
    './component/services',
    './region/routes', './region/services',
    './group/routes', './group/services',
    './importers/routes', './importers/services'


], function(angular) {
  'use strict';

  return angular.module('prm.classification', ['ngCookies', 'ngRoute',
      'componenttype.routes', 'componenttype.services',
      'equipmentstate.routes', 'equipmentstate.services',
      'incidenttype.routes', 'incidenttype.services',
      'residuallifespan.routes', 'residuallifespan.services', 'residuallifespan.edit',
      'repair.services',
      'downtimecost.services',
      'powerunit.routes', 'powerunit.services',
      'powerstation.routes', 'powerstation.services',
      'component.services',
      'region.routes', 'region.services',
      'group.routes', 'group.services' ,
      'importers.routes', 'importers.services'
  ]);
});
