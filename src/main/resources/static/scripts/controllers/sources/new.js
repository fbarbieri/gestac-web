'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SourcesNewCtrl', function ($scope,item,areas,$uibModalInstance) {
	  $scope.title="Fuentes";
	  $scope.item=item;
	  $scope.areas=areas;
	  
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
		  //$scope.item.ownEvaluationTotal = (Number($scope.item.workExperience) + Number($scope.item.areaEducation) + Number($scope.item.title))/Number(3);
		  $uibModalInstance.close($scope.item);
	  };
	  
	  setAreaToSource();
	  
  });
