'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SourceListCtrl', function ($scope,$rootScope,$uibModal,sourcesList,areasList,Sources,Areas) {
	  
	  $scope.gridOptions = {
			    modifierKeysToMultiSelectCells: true,
			    showGridFooter: true
			  };
	  
	  $scope.gridOptions.columnDefs = [
   			    { name: 'name',field:'name',displayName:'Nombre',allowCellFocus : false },
   			    { name: 'mail',field:'mail',displayName:'Mail',allowCellFocus : false },
   			    //{ name: 'area',field:'area.name',displayName:'Area',allowCellFocus : false },
   			    { name: 'edit',cellTemplate:'<a href="" ng-click="grid.appScope.editRow(row.entity)"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>'},
   			    { name: 'remove',cellTemplate:'<a href="" ng-click="grid.appScope.removeRow(row,row.entity)"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'},
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
	  			}
		      }
		    });

		    modalInstance.result.then(function (selectedItem) {
		      selectedItem.$update().then(function (source) {
		    	  $scope.refreshRows();
			  });
		    }, function () {
		      console.log('Modal dismissed at: ' + new Date());
		    });
	  }
	  
// var setAreasToSources = function() {
// areasList = Areas.query();
//		  
// for (var j=0;j<areasList.length;j++) {
//			  var area = areasList[j];
//			  for (var k=0;k<area.sources.length;k++) {
//				  var source = area.sources[k];
//				  for (i=0;i<sourcesList.length;i++) {
//					  var sourceFromList = sourceList[i];
//					  if (source.id==sourceFromList.id) {
//						  sourceFromList.area = area;
//					  }
//				  }
//			  }
//		  }
//	  }
	  
//	  $scope.gridOptions.data = setAreasToSources();
	  
	  $scope.gridOptions.data = sourcesList;
	  
//	  $scope.showSources = function() {
//			for(var i=0;i<sourcesList.length;i++) {
//				console.log(sourcesList[i]);
//			}
//	  };
//	  
//	  $scope.showSources();
	  
	  
	  
  });