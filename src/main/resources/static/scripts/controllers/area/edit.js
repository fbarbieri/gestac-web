'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('AreasEditCtrl', function ($scope,item,$uibModalInstance) {
	  $scope.title="AREA";
	  $scope.item=item;
	  
	  $scope.cancel=function(){
		  $uibModalInstance.dismiss();
	  }
	  
	  $scope.accept=function(){
		  $uibModalInstance.close($scope.item);
	  }
	  
  });
