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
			return toolbox_http.get(jsRoutes.controllers.Listes.liste(id).url);
		}
	    ,
		rechercherListe: function() {
			return toolbox_http.get(jsRoutes.controllers.Listes.listes().url);
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
			return toolbox_http.post(jsRoutes.controllers.Listes.add().url, params);
		}
		,
		modifierListe: function(id, params) {
			return toolbox_http.put(jsRoutes.controllers.Listes.update(id).url, params);
		}		
		,
		supprimerListe: function(id) {
			return toolbox_http.delete(jsRoutes.controllers.Listes.delete(id).url);
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
			return toolbox_http.get(jsRoutes.controllers.Listes.parametres().url);
		}
   }
}]);