'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SubjectListCtrl', function ($scope,$rootScope,$uibModal,areasList,Areas) {
	  
	  $scope.data = {
			  areas : areasList
	  };
	  
	  $scope.data.selectedArea = $scope.data.areas[0];		  
	  
	  $scope.gridOptions = {
		    modifierKeysToMultiSelectCells: true,
		    showGridFooter: true
		  };
	  
	  $scope.gridOptions.columnDefs = [
	    { name: 'name', field:'name',displayName:"Nombre",allowCellFocus : false },
	    { name: 'description', field:'description',displayName:"Descripcion",allowCellFocus : false },
	    { name: 'remove', cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
	  ];
	  
	  $scope.areaChanged = function () {
		  $scope.gridOptions.data = $scope.data.selectedArea.subjects;
	  };
  });