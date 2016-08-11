'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SourcesNewCtrl', function ($scope,item,areas,$uibModalInstance) {
	  $scope.title="Fuentes";
	  $scope.item=item;
	  $scope.areas=areas;
	  
	  $scope.cancel=function(){
		  $uibModalInstance.dismiss();
	  }
	  
	  $scope.accept=function(){
		  $uibModalInstance.close($scope.item);
	  }
	  
  });
