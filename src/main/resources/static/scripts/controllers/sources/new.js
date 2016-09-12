'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SourcesNewCtrl', function ($scope,item,areas,disabled,$uibModalInstance) {
	  $scope.title="Fuentes";
	  $scope.item=item;
	  $scope.areas=areas;
	  $scope.data={
			  errorMessage:null
	  };
	  $scope.areaDisabled = disabled;
	  
	  var setAreaToSource = function() {
		  for (var i=0;i<areas.length;i++) {
			  var area = areas[i];
			  for (var j=0;j<area.sources.length;j++) {
				  if ($scope.item.id == area.sources[j].id) {
					  $scope.item.area = area;
					  break;
				  }
			  }
		  }
	  };
	  
	  $scope.cancel=function(){
		  $uibModalInstance.dismiss();
	  };
	  
	  $scope.accept=function(){
		  if (typeof $scope.item.workExperience === 'undefined' || 
				  typeof $scope.item.workExperience === 'undefined' ||
				  typeof $scope.item.areaEducation === 'undefined' ||
				  typeof $scope.item.title === 'undefined' ||
				  typeof $scope.item.perceptionCommonSense === 'undefined' ||
				  typeof $scope.item.perceptionOrder === 'undefined' ||
				  typeof $scope.item.perceptionInterest === 'undefined' ||
				  typeof $scope.item.perceptionWorkCapacity === 'undefined' ||
				  typeof $scope.item.perceptionGroupWorkCapacity === 'undefined') {
			  $scope.data.errorMessage = 'Debe completar los datos de la evaluación';
		  } else if (typeof $scope.item.name === 'undefined' ||
				  typeof $scope.item.userName === 'undefined' ||
				  typeof $scope.item.password === 'undefined' ||
				  typeof $scope.item.area === 'undefined') {
			  $scope.data.errorMessage = 'Debe completar los datos de la fuente';
		  } else {
			  $uibModalInstance.close($scope.item);			  
		  }
	  };
	  
	  setAreaToSource();
	  
  });
