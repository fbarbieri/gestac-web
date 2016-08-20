'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('NewIssueCtrl', function ($scope,$rootScope,$uibModal,$http,$location,$window,areasList,Sources,Areas) {
	  
	  $scope.areas = areasList;
	  
	  $scope.selectedArea=$scope.areas[0];
	  //como selecciono recién cuando el promise ya se cumplió?
	  
	  $scope.areaSelected = function() {
		  var sa = $scope.selectedArea;
		  $scope.subjects=sa.subjects;
		  $scope.selectedSubject=sa.subjects[0];
		  
		  $scope.incidents=sa.incidents;
		  $scope.selectedIncident=sa.incidents[0];
		  $scope.gravities=$scope.selectedIncident.gravities;
		  $scope.selectedGravity=$scope.selectedIncident.gravities[0];
	  };
	  
	  $scope.incidentSelected = function() {
		  var incident=$scope.selectedIncident;
		  $scope.gravities=incident.gravities;
	  };
	  
	  $scope.saveIssue = function() {
		  if ($scope.selectedArea === undefined) {
			  $scope.fieldsError='Debe seleccionar un área';
			  return;
		  }
		  if ($scope.selectedSubject === undefined) {
			  $scope.fieldsError='Debe seleccionar un sujeto';
			  return;
		  }
		  if ($scope.selectedIncident === undefined) {
			  $scope.fieldsError='Debe seleccionar un incidente';
			  return;
		  }
		  if ($scope.selectedGravity === undefined) {
			  $scope.fieldsError='Debe seleccionar una gravedad';
			  return;
		  }
		  if ($scope.newIssue === undefined || $scope.newIssue.title === undefined) {
			  $scope.fieldsError='Debe ingresar un titulo';
			  return;
		  }
		  if ($scope.newIssue === undefined || $scope.newIssue.description === undefined) {
			  $scope.fieldsError='Debe ingresar una descripción';
			  return;
		  }
		  $scope.fieldsError=null;
	  };
	  
  });