'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('AreasListCtrl', function ($scope,$rootScope,$uibModal,areasList,Areas) {
	  
	  //$rootScope.entity="Mantenimiento de Area";
	  
	  $scope.gridOptions = {
			    modifierKeysToMultiSelectCells: true,
			    showGridFooter: true
			  };
			  $scope.gridOptions.columnDefs = [
			    { name: 'name',/*width:'640', */label:"Nombre",allowCellFocus : false },
			    { name: 'description',/*width:'640', */label:"Descripcion",allowCellFocus : false },
			    { name: 'edit', /*width:'100', */cellTemplate:'<a href="" ng-click="grid.appScope.editRow(row.entity)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'},
			    { name: 'remove', /*width:'100',*/cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
			  ];
			 
			  
			  $scope.refreshRows=function(){
				  $scope.gridOptions.data=Areas.query()
			  }
			  
			  $scope.removeRow=function(row,item) {
				  item.$delete({id:item.name}).then(function (area) {
					  $scope.refreshRows();
				  });
					 
			  }
			  
			  
			  $scope.add=function(){
				  var modalInstance = $uibModal.open({
			    			      animation: $scope.animationsEnabled,
			    			      templateUrl: '/views/areas/edit.html',
			    			      controller: 'AreasEditCtrl',
			    			      size: 'large',
			    			      resolve: {
			    			        item: function (Areas) {
			    			          return new Areas();
			    			        }
			    			      }
			    			    });

			    			    modalInstance.result.then(function (item) {
//			    			    	item.source = {
//			    			    			name: 'namedesource',
//			    			    			mail: 'maildesource'
//			    			    	}
			    			    	item.$save().then(function (area) {
			    			    		 $scope.refreshRows();
			    					  });
			    			    }, function () {
			    			      console.log('Modal dismissed at: ' + new Date());
			    			    });
			  }
			  
			  
			  $scope.editRow=function(item){
				  var modalInstance = $uibModal.open({
			    			      animation: $scope.animationsEnabled,
			    			      templateUrl: '/views/areas/edit.html',
			    			      controller: 'AreasEditCtrl',
			    			      size: 'large',
			    			      resolve: {
			    			        item: function () {
			    			          return item;
			    			        }
			    			      }
			    			    });

			    			    modalInstance.result.then(function (selectedItem) {
			    			      selectedItem.$update().then(function (area) {
			    			    	  $scope.refreshRows();
			    				  });
			    			    }, function () {
			    			      console.log('Modal dismissed at: ' + new Date());
			    			    });
			  }
			  
			  $scope.gridOptions.data = areasList;
			 
			 
  });
