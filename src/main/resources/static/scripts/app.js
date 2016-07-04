'use strict';

/**
 * @ngdoc overview
 * @name gestacWebApp
 * @description
 * # gestacWebApp
 *
 * Main module of the application.
 */
angular
  .module('app', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.grid',
    'ui.grid.cellNav',
    'ui.grid.pinning',
    'ui.bootstrap'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/search/search.html',
        controller: 'SearchCtrl',
        controllerAs: 'main',
        resolve: {
	        areasList: function (Areas) {
	          return Areas.query();
	        }
	      }
      })
      .otherwise({
        redirectTo: '/'
      });
  });
