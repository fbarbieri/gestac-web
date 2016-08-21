'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('IncidentNewCtrl', function ($scope,item,$uibModalInstance) {
	  $scope.item = item;
	  //={name:'', description:'' };
	  
	  $scope.cancel=function(){
		  $uibModalInstance.dismiss();
	  }
	  
	  $scope.accept=function(){
		  $uibModalInstance.close($scope.item);
	  }
	  
  });