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
		rechercherListe: function() {
			var defered = $q.defer();
			defered.resolve(DB.listes);
			return defered.promise;
		}
		,
		creerListe: function(params) {
			var defered = $q.defer();
			defered.resolve("ok");
			return defered.promise;
		}
		,
		supprimerListe: function(id) {
			var defered = $q.defer();
			defered.resolve("ok");
			return defered.promise;
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
			var defered = $q.defer();
			defered.resolve(DB.parametres);
			return defered.promise;
		}
   }
}]);
