'use strict';

/**
 * @ngdoc function
 * @name gestacWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gestacWebApp
 */
angular.module('app')
  .controller('NewKnowledgeCtrl', function ($scope,$rootScope,$uibModal,$http,$location,$window,areasList,sourcesList,Sources,Areas) {
	  
	  $scope.data={
			  areas: areasList,
	  };
	  
	  $scope.newKnowledge={};
	  $scope.newKnowledgeEvaluation={};
	  
	  $scope.back = function() {
		  var home = $window.location.host + "/#";
		  $location.path('/#');
	  };
	  
	  var refresh = function() {
		  var urlParams = ''+$scope.currentSource.id;
		  $http.get('/sources/isBestSource/'+urlParams).then(function(data) {
			  if (data.data=='') {
				  //no es la mejor fuente para el área, mensaje y volver
				  $scope.noBestSource = "No es la mejor fuente para su área, no puede ingresar conocimientos";
			  } else {
				  $scope.issuesNoKnowledge = data.data;
			  }
			  
			  
			  if (data.data!='') { //es mejor fuente para área, y vienen los problemas sin respuesta o con respuesta de otro
				  $scope.open("Evaluación", "Evaluación agregada correctamente");
			  } else {
				  
			  }
	        });
	  }
	 
	  $scope.openLogin = function() {
		  
		  var modalInstance = $uibModal.open({
			 animation:false,
			 templateUrl:'/views/login/loginModal.html',
			 controller:'LoginModalInstanceCtrl',
			 size:'sm',
			 resolve: {
				modal:{
			          text:'Ingrese su identificación de fuente',
			          title:'Login'
			        } 
		      }
		  });
		  
		  modalInstance.result.then(function(item){
			  $scope.currentSource = item;
			  
			  refresh();
			  
		  }, function(){
			  console.log('Modal dismissed at: ' + new Date());
			  var home = $window.location.host + "/#";
			  $location.path('/#');
		  });		  
		  
	  };
	  
	  $scope.selectIssue = function(item){
		  $scope.selectedIssue=item;
	  };
	  
	  $scope.saveKnowledge = function() {
		  $http.post('/knowledge/newKnowledge/', {
			  knowledge:$scope.newKnowledge,
			  evaluation:$scope.newKnowledgeEvaluation,
			  issue:$scope.selectedIssue,
			  source:$scope.currentSource
		  }).then(function(data) {
			  if (data.data!=null) {
				  $scope.newKnowledgeError = false;
				  $scope.selectedIssue = null;
				  refresh();				  
			  } else {
				  $scope.newKnowledgeError=true;
			  }
		  });
	  };
	  
	  $scope.openLogin();
	  
	  
  });