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
	  }
	  
	  $scope.back = function() {
		  var home = $window.location.host + "/#";
		  $location.path('/#');
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
			  
			  var urlParams = ''+$scope.currentSource.id;
			  $http.get('/sources/isBestSource/'+urlParams).then(function(data) {
				  if (data.data!=null) { //es mejor fuente para área, y vienen los problemas sin respuesta o con respuesta de otro
					  $scope.open("Evaluación", "Evaluación agregada correctamente");
				  } else {
					  //no es la mejor fuente para el área, mensaje y volver
					  $scope.noBestSource = "No es la mejor fuente para su área, no puede ingresar conocimientos";
				  }
		        });
			  
		  }, function(){
			  console.log('Modal dismissed at: ' + new Date());
			  var home = $window.location.host + "/#";
			  $location.path('/#');
		  });		  
		  
	  };
	  
	  $scope.openLogin();
	  
	  
  });