'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('GravityListCtrl', function ($scope,$rootScope,$uibModal,$window,areasList,Areas,Gravities) {
	  
	  $scope.data = {
			  areas : areasList
	  };
	  
	  $scope.refreshRows=function(){
		  $scope.data.areas = Areas.query();
	  }
	  
	  $scope.data.selectedArea = $scope.data.areas[0];	  
	  
	  $scope.gridOptions = {
		    modifierKeysToMultiSelectCells: true,
		    showGridFooter: true
		  };
	  
	  $scope.gridOptions.columnDefs = [
	    //{ name: 'name', field:'name',displayName:"Nombre",allowCellFocus : false },
	    { name: 'description', field:'description',displayName:"Descripcion",allowCellFocus : false },
	    { name: 'remove', cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
	  ];
	  
	  $scope.areaChanged = function () {
		  if (typeof $scope.data.selectedArea != 'undefined') {
			  $scope.data.incidents = $scope.data.selectedArea.incidents;
		  }
	  };
	  
	  $scope.incidentChanged = function() {
		  if (typeof $scope.data.selectedIncident != 'undefined') {
			  $scope.gridOptions.data = $scope.data.selectedIncident.gravities;
		  }
	  }
	  
	  
	  $scope.add=function(){
		  if (typeof $scope.data.selectedArea!='undefined'){
			  var modalInstance = $uibModal.open({
			      animation: $scope.animationsEnabled,
			      templateUrl: '/views/gravities/new.html',
			      controller: 'GravitiesNewCtrl',
			      size: 'medium',
			      resolve: {
			    	  item: function (Gravities) {
    			          return new Gravities();
    			        }
			      }
			    });

			    modalInstance.result.then(function (item) {
			    	item.incident=$scope.data.selectedIncident;
			    	item.$save().then(function (gravity) {
			    		 $scope.data.selectedIncident.gravities.push(gravity);
			    		 $scope.refreshRows();
			    		 $scope.data.selectedArea = $scope.data.areas[0];
			    		 $window.location.reload();
					  },function(gravity){
						  $scope.open("Gravedad", "Ya existe una gravedad con esos datos, ingrese otros.");
					  });
			    }, function () {
			      console.log('Modal dismissed at: ' + new Date());
			    });
		  } 
	  }
	  
	  $scope.removeRow=function(row,item) {
		  var gravity = new Gravities();
		  gravity.id = item.id;
		  gravity.$delete().then(function (gravity) {
			  $scope.refreshRows();
			  $window.location.reload();
		  }, function(gravity) {
			  $scope.open("Gravedad", "No se puede eliminar la gravedad porque está en uso");
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
		  
  });