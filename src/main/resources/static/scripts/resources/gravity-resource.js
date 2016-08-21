angular.module('app').factory('Gravities', ['$resource', function($resource) {
	return $resource('/gravities/:name', {name:'@id'},{
		'update': { method:'PUT' }
	});
}]);