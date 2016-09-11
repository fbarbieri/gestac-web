'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('IncidentListCtrl', function ($scope,$rootScope,$uibModal,$window,areasList,Areas,Incidents) {
	  
	  $scope.data = {
			  areas : areasList
	  };
	  
	  $scope.refreshRows=function(){
		  $scope.data.areas = Areas.query();
//		  Areas.query().$promise.then(function(result){
//			  $scope.data.areas = result;
//			  $scope.data.selectedArea = $scope.data.areas[0];
//			  $scope.gridOptions.data = $scope.data.selectedArea.incidents;
//			  $scope.areaChanged();
//		  });
	  }
	  
	  $scope.data.selectedArea = $scope.data.areas[0];		  
	  
	  $scope.gridOptions = {
		    modifierKeysToMultiSelectCells: true,
		    showGridFooter: true
		  };
	  
	  $scope.gridOptions.columnDefs = [
	    { name: 'name', field:'name',displayName:"Nombre",allowCellFocus : false },
	    { name: 'description', field:'description',displayName:"Descripcion",allowCellFocus : false },
	    { name: 'remove', displayName:"Borrar",cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
	  ];
	  
	  $scope.areaChanged = function () {
		  if (typeof $scope.data.selectedArea != 'undefined') {
			  $scope.gridOptions.data = $scope.data.selectedArea.incidents;
		  }
	  };
	  
	  $scope.add=function(){
		  if (typeof $scope.data.selectedArea!='undefined'){
			  var modalInstance = $uibModal.open({
			      animation: $scope.animationsEnabled,
			      templateUrl: '/views/incidents/new.html',
			      controller: 'IncidentNewCtrl',
			      size: 'medium',
			      resolve: {
			    	  item: function (Incidents) {
    			          return new Incidents();
    			        }
			      }
			    });

			    modalInstance.result.then(function (item) {
			    	item.area=$scope.data.selectedArea;
			    	item.$save().then(function (incident) {
			    		 $scope.data.selectedArea.incidents.push(incident);
			    		 $scope.refreshRows();
			    		 $scope.data.selectedArea = $scope.data.areas[0];
			    		 $window.location.reload();
					  },function(gravity){
						  $scope.open("Incidente", "Ya existe un incidente con esos datos, ingrese otros.");
					  });
			    }, function () {
			      console.log('Modal dismissed at: ' + new Date());
			    });
		  } 
	  }
	  
	  $scope.removeRow=function(row,item) {
		  var incident = new Incidents();
		  incident.id = item.id;
		  incident.$delete().then(function (incident) {
			  $scope.refreshRows();
			  $window.location.reload();
		  }, function(incident) {
			  $scope.open("Incidente", "No se puede eliminar el incidente porque está en uso");
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