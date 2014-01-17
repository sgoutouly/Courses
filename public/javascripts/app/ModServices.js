/**
 * Module contenant les services de l'application
 */
var modServices = angular.module("courses.services", []);
/**
 * Service rechercherListe
 * Enregistre la lise en cours de saisie
 */
modServices.factory("ComposantListe", ["$q", "$http", function($q, $http) {
   return {
		consulterListe: function(id) {
			var defer = $q.defer()
			$http.get("/mongo/collections/listes/" + id).success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
				alert("Erreur : " + status);
			});
			return defer.promise;
		}
		,   	
		rechercherListe: function() {
			var defer = $q.defer()
			$http.get("/mongo/collections/listes").success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
			    alert("Erreur : " + status);
			});
			return defer.promise;
		}
		,
		creerListe: function(params) {
			var defer = $q.defer()
			$http.post("/mongo/collections/listes", params).success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
			    alert("Erreur : " + status);
			});
			return defer.promise;
		}
		,
		modifierListe: function(id, params) {
			var defer = $q.defer()
			$http.put("/mongo/collections/listes/" + id, params).success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
			    alert("Erreur : " + status);
			});
			return defer.promise;
		}		
		,
		supprimerListe: function(id) {
			var defer = $q.defer();
			$http.delete("/mongo/collections/listes/" + id).success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
			    alert("Erreur : " + status);
			});
			return defer.promise;
		}
   }
}]);
/**
 * Service ajouterCourse
 * Ajoute une course à la collection passée en argument
 */
modServices.factory("ajouterCourse", function() {
	return function(courses, params) {
		courses.push(
			{designation : params.designation, qte : params.qte, unite: params.unite}
		)
	}
});
/**
 * Service de lecture des paramètres
 */
modServices.factory("ComposantParametres", ["$q", "$http", function($q, $http) {
   return {
		lire: function() {
			var defer = $q.defer();
			$http.get("/mongo/collections/parametres/first").success(function(data) {
				defer.resolve(data);
			}).error(function(status) {
			    alert("Erreur : " + status);
			});
			return defer.promise;
		}
   }
}]);