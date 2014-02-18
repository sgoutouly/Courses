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
			return $http.get("/mongo/collections/listes/" + id)
				.then(function(result) {return result.data;}, 
					function(result) {alert("Erreur : " + result.status + ", " + result.data);});
		}
		,   	
		rechercherListe: function() {
			return $http.get("/mongo/collections/listes")
				.then(function(result) {return result.data;}, function(result) {alert("Erreur : " + result.status + ", " + result.data);});
		}
		,
		copierListe: function(id) {	
			var	that = this;
			return that.consulterListe(id)
				.then(function(listeCopiee) {
					return that.creerListe({dateRedaction: new Date().toLocaleDateString(), courses: listeCopiee.courses})
				})
				.then(function (headers) {
					return $http.get(headers.location).then(function(result) {return result.data;}, function(result) {alert("Erreur : " + result.status + ", " + result.data);});
				});
		}
		,
		creerListe: function(params) {
			return $http.post("/mongo/collections/listes", params)
				.then(function(result) {return result.headers();}, function(result) {alert("Erreur : " + result.status + ", " + result.data);});
		}
		,
		modifierListe: function(id, params) {
			return $http.put("/mongo/collections/listes/" + id, params)
				.then(function(result) {return result.data;}, function(result) {alert("Erreur : " + result.status + ", " + result.data);});
		}		
		,
		supprimerListe: function(id) {
			return $http.delete("/mongo/collections/listes/" + id)
				.then(function(result) {return result.data;}, function(result) {alert("Erreur : " + result.status + ", " + result.data);});
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
			return $http.get("/mongo/collections/parametres/first")
				.then(function(result) {return result.data;}, function(result) {alert("Erreur : " + result.status);});
		}
   }
}]);