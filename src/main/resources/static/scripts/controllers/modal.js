angular.module('app')
	.controller('ModalInstanceCtrl', function($scope, $uibModalInstance, item){
	
	$scope.item=item;
		
	$scope.ok = function(){
		$uibModalInstance.close();
	};
	
	$scope.cancel = function(){
		$uibModalInstance.dismiss('cancel');
	};
		
});