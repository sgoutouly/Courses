/**
 * Module contenant les services utilitaires
 */
var toolBoxServices = angular.module("ToolBox.services", []);

/**
 * Service http_tb
 * Ajoute des fonctionnalités à $http (gestion des erreurs)
 */
toolBoxServices.factory('toolbox_http',['$http',function($http) {

	function handleError(result) {
		alert("Erreur : " + result.status + ", " + result.data);
	}

  	return {
	    get : function(url) {
			return $http.get(url).then(	
				function(result) {return result.data;}, 
				function(result) {handleError(result);}
			)
	    }
	    ,
	    put : function(url, params) {
			return $http.put(url, params).then(
				function(result) {return result.data;}, 
				function(result) {handleError(result);}
			)
	    }
	    ,
	    post : function(url, params) {
			return $http.post(url, params).then(	
				function(result) {return result.headers();}, 
				function(result) {handleError(result);}
			);
	    }
	    ,
	    delete : function(url) {
			return $http.delete(url).then(	
				function(result) {return result.data;}, 
				function(result) {handleError(result);}
			)
	    }
  	};
}]);

