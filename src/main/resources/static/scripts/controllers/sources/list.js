'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SourceListCtrl', function ($http,$scope,$rootScope,$uibModal,sourcesList,areasList,Sources,Areas) {
	  
	  $scope.gridOptions = {
			    modifierKeysToMultiSelectCells: true,
			    showGridFooter: true
			  };
	  
	  $scope.gridOptions.columnDefs = [
   			    { name: 'name',field:'name',displayName:'Nombre',allowCellFocus : false },
   			    { name: 'mail',field:'mail',displayName:'Mail',allowCellFocus : false },
   			    { name: 'userName',field:'userName',displayName:'Login',allowCellFocus : false },
   			    { name: 'scoreTotal',field:'scoreTotal',displayName:'Puntuación Total',allowCellFocus : false },
   			    { name: 'ownScore',field:'ownEvaluationTotal',displayName:'Puntuación Propia',allowCellFocus : false },
   			    //{ name: 'area',field:'area.name',displayName:'Area',allowCellFocus : false },
   			    { name: 'edit',displayName:"Editar",cellTemplate:'<a href="" ng-click="grid.appScope.editRow(row.entity)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'},
   			    { name: 'remove',displayName:"Borrar",cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
   			  ];
	  
	  $scope.refreshRows=function(){
		  $scope.gridOptions.data=Sources.query();
		  areasList = Areas.query();
	  }
	  
	  $scope.add=function(){
		  var modalInstance = $uibModal.open({
			  	animation : $scope.animationsEnabled,
			  	templateUrl : '/views/sources/new.html',
			  	controller : 'SourcesNewCtrl',
			  	size : 'lg',
			  	resolve : {
			  			item : function(Sources) {
			  				return new Sources();
			  			},
			  			areas : function(Areas) {
			  				return areasList;
			  			},
			  			disabled : function() {
			  				return false;
			  			}
			  		}
			  	});

				modalInstance.result.then(function(item) {
//					  $http.post('/sources',item).then(function(data) {
//						  console.log(data);
//				        }); 
					item.$save().then(function(source) {
						$scope.refreshRows();
					});
				}, function() {
					console.log('Modal dismissed at: ' + new Date());
				});
	  }
	  
	  $scope.editRow=function(item){
		  var modalInstance = $uibModal.open({
		      animation: $scope.animationsEnabled,
		      templateUrl: '/views/sources/new.html',
		      controller: 'SourcesNewCtrl',
		      size: 'lg',
		      resolve: {
		    	item : function() {
		    		return item;
	  			},
	  			areas : function(Areas) {
	  				return areasList;
	  			},
	  			disabled : function() {
	  				return true;
	  			}
		      }
		    });

		    modalInstance.result.then(function (selectedItem) {
		    	$http.put('/sources/', selectedItem).then(function(data) {
		    		$scope.refreshRows();
				  });
		    }, function () {
		      console.log('Modal dismissed at: ' + new Date());
		      $scope.refreshRows();
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
	  
	  $scope.removeRow=function(row, item) {
		  item.$delete().then(function (source){
			  $scope.refreshRows();				  
		  }, function(source){
			  $scope.open("Fuentes", "No se puede eliminar la fuente porque está en uso");
		  });
	  }
	  
	  
	  
	  $scope.gridOptions.data = sourcesList;
	  

	  
	  
	  
  });