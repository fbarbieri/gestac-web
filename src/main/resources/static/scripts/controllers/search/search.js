'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SearchCtrl', function ($scope,$rootScope,$uibModal,$http,areasList,Areas,Subjects) {
	  
	  $rootScope.entity="Gestac";
	  
	  $scope.data={
			  areas: areasList,
	  }
	  $scope.data.selectedArea=$scope.data.areas[0];
	  
	  $scope.areaSelected = function(){
		  var sa = $scope.data.selectedArea;
		  $scope.data.subjects=sa.subjects;
		  $scope.data.selectedSubject=sa.subjects[0];
		  
		  $scope.data.incidents=sa.incidents;
		  $scope.data.selectedIncident=sa.incidents[0];
		  $scope.data.selectedGravity=$scope.data.selectedIncident.gravities[0];
	  }
	  
	  $scope.incidentSelected = function(){
		  var incident=$scope.data.selectedIncident;
		  $scope.data.gravities=incident.gravities;
	  }
	  
	  $scope.searchIssue = function() {
		  //$http.get('/search/test').then(function(data) {
		  var urlParams = ''+$scope.data.selectedArea.id+'/'+$scope.data.selectedSubject.id+'/'+$scope.data.selectedIncident.id+'/'+$scope.data.selectedGravity.id;
		  $http.get('/search/getIssuesForAreaSubjectIncidentGravity/'+urlParams).then(function(data) {
			  console.log(data);
	          alert(data);  
			  $scope.greeting = data;
	        });
	  }
	  
  });
  
  