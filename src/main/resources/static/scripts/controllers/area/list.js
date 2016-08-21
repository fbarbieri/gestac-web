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
			    { name: 'name', field:'name',displayName:"Nombre",allowCellFocus : false }, /*width:'640', */
			    { name: 'description', field:'description', displayName:"Descripcion",allowCellFocus : false },
			    //{ name: 'edit', /*width:'100', */cellTemplate:'<a href="" ng-click="grid.appScope.editRow(row.entity)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'},
			    { name: 'remove', cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
			  ];
			 
			  
			  $scope.refreshRows=function(){
				  $scope.gridOptions.data=Areas.query()
			  }
			  
			  $scope.removeRow=function(row,item) {
				  item.$delete().then(function (area) {
					  $scope.refreshRows();
				  }, function(area) {
					  $scope.open("Area", "No se puede eliminar el área porque está en uso");
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
			  
			  $scope.open = function(modalTitle, modalText) {
				  
				  var modalInstance = $uibModal.open({
					 animation:false,
					 templateUrl:'/views/modal.html',
					 controller:'ModalInstanceCtrl',
					 size:'large',
					 resolve: {
				        item: {
				          text:modalText,
				          title:modalTitle
				        }
				      }
				  });
				  
				  modalInstance.result.then(function(){
					  $window.location.reload();
				  });
				  
			  };
			  
			  $scope.gridOptions.data = areasList;
			 
			 
  });
