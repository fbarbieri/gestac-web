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
				  for (var i=0;i<areasList.length;i++) {
					  var area = areasList[i];
					  for (var j=0;j<area.sources.length;j++) {
						  if ($scope.currentSource.id == area.sources[j].id) {
							  $scope.currentSource.area = area;
							  break;
						  }
					  }
				  }
			  }
	        });
	  }
	  
	  $scope.getIssuesWithKnowledge = function() {
		  $scope.noIssuesWithKnowledgeByOthers = false;
		  var urlParams = ''+$scope.currentSource.id+'/'+$scope.currentSource.area.id+'/';
		  $http.get('/sources/getIssuesWithKnowledge/'+urlParams).then(function(data) {
			 if (data.data!='') {
				 $scope.issuesWithKnowledgeByOthers = data.data;
			 } else {
				 $scope.noIssuesWithKnowledgeByOthers = true;
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