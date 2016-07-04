angular.module('app').factory('Subjects', ['$resource', function($resource) {
	return $resource('/subjects/:name', {name:'@id'},{
		'update': { method:'PUT' }
	});
}]);