'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('SearchCtrl', function ($scope,$rootScope,$uibModal,$http,areasList,Areas,Subjects,$location,$window) {
	  
	  $rootScope.entity="Gestac v2";
	  
	  $scope.isActive = function (viewLocation) {
		     var active = (viewLocation === $location.path());
		     return active;
	  };
	  
	  $scope.data={
			  areas: areasList,
	  }
	  $scope.data.areas.$promise.then(function(result){
		  $scope.data.selectedArea=$scope.data.areas[0];
		  $scope.areaSelected();
	  });
	  //$scope.data.selectedArea=$scope.data.areas[0];
	  //como selecciono recién cuando el promise ya se cumplió?
	  
	  $scope.areaSelected = function(){
		  var sa = $scope.data.selectedArea;
		  $scope.data.subjects=sa.subjects;
		  $scope.data.selectedSubject=sa.subjects[0];
		  
		  $scope.data.incidents=sa.incidents;
		  $scope.data.selectedIncident=sa.incidents[0];
		  if (typeof $scope.data.selectedIncident != 'undefined') {
			  $scope.data.gravities=$scope.data.selectedIncident.gravities;
			  $scope.data.selectedGravity=$scope.data.selectedIncident.gravities[0];			  
		  }else{
			  $scope.data.gravities=undefined;
		  }
	  }
	  
	  $scope.incidentSelected = function(){
		  var incident=$scope.data.selectedIncident;
		  $scope.data.gravities=incident.gravities;
	  }
	  
	  $scope.searchIssue = function() {
		  if (typeof $scope.data.selectedArea == 'undefined') {
			  $scope.data.searchError = 'Debe seleccionar un área';
			  return;
		  }
		  if (typeof $scope.data.selectedSubject == 'undefined') {
			  $scope.data.searchError = 'Debe seleccionar un sujeto';
			  return;
		  }
		  if (typeof $scope.data.selectedIncident == 'undefined') {
			  $scope.data.searchError = 'Debe seleccionar un incidente';
			  return;
		  }
		  if (typeof $scope.data.selectedGravity == 'undefined') {
			  $scope.data.searchError = 'Debe seleccionar una gravedad';
			  return;
		  }
		  var urlParams = ''+$scope.data.selectedArea.id+'/'+$scope.data.selectedSubject.id+'/'+$scope.data.selectedIncident.id+'/'+$scope.data.selectedGravity.id;
		  $http.get('/search/getIssuesForAreaSubjectIncidentGravity/'+urlParams).then(function(data) {
			  console.log(data);
			  $scope.data.searchedIssues = data.data;
			  if ($scope.data.searchedIssues!=null && $scope.data.searchedIssues!='') {
				    $scope.data.searchError = null; 
					$scope.data.displayIssue = $scope.data.searchedIssues[0];
				} else {
					$scope.data.searchError = 'No hay problemas para esta búsqueda';
					$scope.data.displayIssue = null;
				}
	        });  	  
	  }
	  
	  $scope.getBestKnowledgeForIssue = function() {
		  //$http.get('/search/test').then(function(data) {
		  var urlParams = ''+$scope.data.displayIssue.id;
		  $http.get('/knowledge/bestForIssue/'+urlParams).then(function(data) {
			  console.log(data);
			  $scope.data.displayKnowledge = data.data;
	        });  	  
	  }
	  
	  $scope.doEvaluation = function() {
		  $scope.data.evaluationError = null;
		  var urlParams = ''+$scope.data.displayKnowledge.id+'/'+$scope.data.selectedSimplicity+'/'+$scope.data.selectedUsedTime+'/'+$scope.data.selectedReuse+'/';
		  $http.get('/knowledge/addEvaluationToKnowledge/'+urlParams).then(function(data) {
			  console.log(data);
			  if (data.data==true) {
				  $scope.open("Evaluación", "Evaluación agregada correctamente");
			  } else {
				  $scope.data.evaluationError = "Debe seleccionar opciones válidas de evaluación";
			  }
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

  