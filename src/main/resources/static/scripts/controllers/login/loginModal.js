'use strict';

angular.module('app')
	.controller('LoginModalInstanceCtrl', function($scope, $uibModalInstance, $http, modal){
	
	$scope.modal = modal;
		
	$scope.item={
			userName:'',
			password:''
	};
	
	$scope.ok = function(){
		$http.post('/login/newKnowledgeLogin/',$scope.item).then(function(data) {
			  if (data.data!=null) {
				  $uibModalInstance.close(data.data);				  
			  } else {
				  $scope.modal.loginError = 'Los datos de login no son correctos';
			  }
		  }); 
	};
	
	$scope.cancel = function(){
		$uibModalInstance.dismiss('cancel');
	};
		
});