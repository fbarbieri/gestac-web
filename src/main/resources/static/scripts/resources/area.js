angular.module('app').factory('Areas', ['$resource', function($resource) {
	return $resource('/areas/:name', {name:'@id'},{
		'update': { method:'PUT' }
	});
}]);