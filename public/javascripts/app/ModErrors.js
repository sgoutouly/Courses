/**
 * Module de gestion des erreurs de routage
 */
var modErrors = angular.module("courses.errors", []);

/* 
 * Routage de ces contrôleurs
 */	
coursesApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.when('/404', {
			templateUrl: 'partials/errors.html',
			controller : 'ErrorCtrl'
		}).
		otherwise({
			redirectTo : '/404'
		});
	}
])

/**
 * Gestion des erreurs globales
 */
coursesApp.run(['$rootScope','$location',function($rootScope, $location) {
  $rootScope.$on("$routeChangeError", function (event, current, previous, rejection) {
    //change this code to handle the error somehow
    $location.path('/404').replace();
  });
}]);

/**
 * Contoleur de la page d'erreur
 */
modErrors.controller("ErrorCtrl", ["$scope", function($scope){
	}
])