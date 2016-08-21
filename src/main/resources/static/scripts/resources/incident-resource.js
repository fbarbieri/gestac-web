angular.module('app').factory('Incidents', ['$resource', function($resource) {
	return $resource('/incidents/:name', {name:'@id'},{
		'update': { method:'PUT' }
	});
}]);