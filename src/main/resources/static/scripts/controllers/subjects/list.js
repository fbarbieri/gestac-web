'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SubjectListCtrl', function ($scope,$rootScope,$uibModal,$window,areasList,Areas,Subjects) {
	  
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
	    { name: 'name', field:'name',displayName:"Nombre",allowCellFocus : false },
	    { name: 'description', field:'description',displayName:"Descripcion",allowCellFocus : false },
	    { name: 'remove', displayName:"Borrar", cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
	  ];
	  
	  $scope.areaChanged = function () {
		  if (typeof $scope.data.selectedArea != 'undefined') {
			  $scope.gridOptions.data = $scope.data.selectedArea.subjects;
		  }
	  };
	  
	  $scope.add=function(){
		  if (typeof $scope.data.selectedArea!='undefined'){
			  var modalInstance = $uibModal.open({
			      animation: $scope.animationsEnabled,
			      templateUrl: '/views/subjects/new.html',
			      controller: 'SubjectNewCtrl',
			      size: 'medium',
			      resolve: {
			    	  item: function (Subjects) {
    			          return new Subjects();
    			        }
			      }
			    });

			    modalInstance.result.then(function (item) {
			    	item.area=$scope.data.selectedArea;
			    	item.$save().then(function (subject) {
			    		 $scope.data.selectedArea.subjects.push(subject);
			    		 $scope.refreshRows();
			    		 $scope.data.selectedArea = $scope.data.areas[0];
			    		 $window.location.reload();
					  });
			    }, function () {
			      console.log('Modal dismissed at: ' + new Date());
			    });
		  } 
	  }
	  
	  $scope.removeRow=function(row,item) {
		  var subject = new Subjects();
		  subject.id = item.id;
		  subject.$delete().then(function (subject) {
			  $scope.refreshRows();
			  $window.location.reload();
		  }, function(subject) {
			  $scope.open("Sujeto", "No se puede eliminar el sujeto porque está en uso");
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