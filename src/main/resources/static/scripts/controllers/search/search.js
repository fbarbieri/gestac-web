'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SearchCtrl', function ($scope,$rootScope,$uibModal,areasList,Areas,Subjects) {
	  
	  $rootScope.entity="Gestac";
	  
//	  $scope.gridOptions = {
//			    modifierKeysToMultiSelectCells: true,
//			    showGridFooter: true
//			  };
	  
	  $scope.data={
			  areas: areasList,
	  }
	  $scope.data.selectedArea=$scope.data.areas[0];
	  
	  $scope.areaSelected = function(){
		  var sa = $scope.data.selectedArea;
		  $scope.data.subjects=sa.subjects;
		  $scope.data.selectedSubject=sa.subjects[0];
		  
		  $scope.data.incidents=sa.incidents;
		  $scope.data.selectedIncident=sa.incidents[0];
	  }
	  
	  
  });
  
  