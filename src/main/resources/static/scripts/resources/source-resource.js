angular.module('app').factory('Sources', ['$resource', function($resource) {
	return $resource('/sources/:name', {name:'@id'},{
		'update': { method:'PUT' }
	});
}]);