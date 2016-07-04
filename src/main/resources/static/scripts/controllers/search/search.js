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
	  
	  $rootScope.entity="Prueba Busqueda!";
	  
	  $scope.gridOptions = {
			    modifierKeysToMultiSelectCells: true,
			    showGridFooter: true
			  };
	  
	  $scope.gridOptions.columnDefs = [
		    { name: 'id',width:'120', label:"Id",allowCellFocus : false },
		    { name: 'name',width:'120', label:"Nombre",allowCellFocus : false },
		    { name: 'description',width:'120', label:"Descripcion",allowCellFocus : false },
//		    { name: 'edit', width:'100', cellTemplate:'<a href="" ng-click="grid.appScope.editRow(row.entity)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'},
//		    { name: 'remove', width:'100',cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
		  ];
	  
	  
	  $scope.gridOptions.data = areasList;
	  
	  $scope.data={
			  areas: areasList,
	  }
	  
	  $scope.areaSelected = function(){
		  var a = $scope.data.selectedArea;
		  $scope.data.subjects=a.subjects;
		  $scope.data.selectedSubject=a.subjects[0];
	  }
	  
	  $scope.data.selectedArea=$scope.data.areas[0];
	  
  });
  
  