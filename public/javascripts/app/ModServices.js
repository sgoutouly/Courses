/**
 * Module contenant les services de l'application
 */
var modServices = angular.module("courses.services", []);

/**
 * Service rechercherListe
 * Enregistre la lise en cours de saisie
 */
modServices.factory("ComposantListe", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
		consulterListe: function(id) {
			return toolbox_http.get("/mongo/collections/listes/" + id);
		}
		,   	
		rechercherListe: function() {
			return toolbox_http.get("/mongo/collections/listes");
		}
		,
		copierListe: function(id) {	
			var	that = this;
			return that.consulterListe(id).then(
				function(listeCopiee) {
					return that.creerListe({dateRedaction: new Date().toLocaleDateString(), courses: listeCopiee.courses})
				}).then(
				function (headers) {
					return toolbox_http.get(headers.location);
				});
		}
		,
		creerListe: function(params) {
			return toolbox_http("/mongo/collections/listes", params);
		}
		,
		modifierListe: function(id, params) {
			return toolbox_http.put("/mongo/collections/listes/" + id, params);
		}		
		,
		supprimerListe: function(id) {
			return toolbox_http.delete("/mongo/collections/listes/" + id);
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
modServices.factory("ComposantParametres", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
		lire: function() {
			return toolbox_http.get("/mongo/collections/parametres/first");
		}
   }
}]);