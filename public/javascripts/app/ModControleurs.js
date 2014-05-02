/**
 * Module contenant les controleurs de l'application
 */
var modControleurs = angular.module("courses.controleurs", []);
/* 
 * Routage de ces contrôleurs
 */	
coursesApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/creationListe', {
    				templateUrl: 'partials/creationListe.html',
    				controller: 'CreationListeCtrl'
  				}).
  				when('/modificationListe/:listeId', {
    				templateUrl: 'partials/creationListe.html',
    				controller: 'ModificationListeCtrl'
  				}).  				
  		      	when('/listesEnCours', {
        			templateUrl: 'partials/listesEnCours.html',
        			controller: 'AffichageListesEnCoursCtrl'
      			}).     
      			when('/listes/:listeId', {
        			templateUrl: 'partials/liste-detail.html',
        			controller: 'ListeDetailCtrl'
      			})
		}
])
/**
 * Contoleur de l'écran de saisie de liste
 * @utilise le service creerListe
 */
modControleurs.controller("CreationListeCtrl", ["$scope", "ComposantParametres", "ComposantListe", "ajouterCourse", 
	function($scope, ComposantParametres, ComposantListe, ajouterCourse){
	
	/* Enrichissement du scope */
	$scope.dateRedaction = new Date().toLocaleDateString();
	$scope.formDesignation;
	$scope.formQte;
	$scope.formUnite;
	$scope.formCourses = [];
	$scope.messageWait = "Chargement des données ..."
 	
	ComposantParametres.lire().then(
		function(data) {
			$scope.paramListeProduits = [{designation:"ajouter un nouveau produit"}].concat(data.produits);
			$scope.paramListeUnites = [{designation:"ajouter une nouvelle unité"}].concat(data.unites);
			$scope.messageWait = "";
		}
	);
  	/**
  	 * Ajoute un course à la liste
  	 */
  	  	/**
  	 * Ajoute un course à la liste
  	 */
  	$scope.addCourse = function() {
		ajouterCourse($scope.formCourses, {
			designation : $scope.formDesignation.designation, 
			qte : $scope.formQte,
			unite: $scope.formUnite.designation
		})
		$scope.formDesignation = "";
		$scope.formQte = "";
		$scope.formUnite = "";
	}
	/**
  	 * Effacer une course de la liste
  	 */
	$scope.deleteCourse = function(indexCourse) {
		$scope.formCourses.splice(indexCourse, 1);
	}
  	/**
  	 * Enregistrer une nouvelle liste
  	 */
	$scope.saveListe = function() {
		ComposantListe.creerListe({
			dateRedaction: $scope.dateRedaction,
			courses: $scope.formCourses
		}).then( 
			function() {
				$scope.formDesignation = "";
				$scope.formQte = "";
				$scope.formCourses = [];
				location.hash = "#/listesEnCours";
			}
		);
	}
	/**
  	 * Retour au menu principal
  	 */
	$scope.goHome = function() {
		location.hash = "#/listesEnCours";
	}
}]);
/**
 * Contoleur de l'écran de saisie de liste
 * @utilise le service creerListe
 */
modControleurs.controller("ModificationListeCtrl", ["$scope", "$routeParams", "ComposantParametres", "ComposantListe", "ajouterCourse", 
	function($scope, $routeParams, ComposantParametres, ComposantListe, ajouterCourse){
	
	/* Enrichissement du scope */
	$scope.formListeId = $routeParams.listeId;
	$scope.formDesignation;
	$scope.formQte;
	$scope.formUnite;
	$scope.messageWait = "Chargement des données ..."
	ComposantListe.consulterListe($scope.formListeId).then(
		function(data) {
			$scope.dateRedaction = data.dateRedaction;
			$scope.formCourses = data.courses;
		}
	);	 	
	ComposantParametres.lire().then(
		function(data) {
			$scope.paramListeProduits = [{designation:"ajouter un nouveau produit"}].concat(data.produits);
			$scope.paramListeUnites = [{designation:"ajouter une nouvelle unité"}].concat(data.unites);
			$scope.messageWait = "";
		}
	);
  	/**
  	 * Ajoute un course à la liste
  	 */
  	$scope.addCourse = function() {
		ajouterCourse($scope.formCourses, {
			designation : $scope.formDesignation.designation, 
			qte : $scope.formQte,
			unite: $scope.formUnite.designation
		})
		$scope.formDesignation = "";
		$scope.formQte = "";
		$scope.formUnite = "";
	}
	/**
  	 *Supprime une course de la liste
  	 */
	$scope.deleteCourse = function(indexCourse) {
		$scope.formCourses.splice(indexCourse, 1);
	}
  	/**
  	 * Enregistrer une liste existante
  	 */
	$scope.saveListe = function() {
		ComposantListe.modifierListe(
			$scope.formListeId,
			{
				dateRedaction: $scope.dateRedaction,
				courses: $scope.formCourses
			}
		).then( 
			function() {
				$scope.formDesignation = "";
				$scope.formQte = "";
				$scope.formCourses = [];
				location.hash = "#/listesEnCours";
			}
		);
	}
	/**
  	 *
  	 */
	$scope.goHome = function() {
		location.hash = "#/listesEnCours";
	}
}]);
/**
 * Contoleur de l'écran de listage des listes
 */
modControleurs.controller("AffichageListesEnCoursCtrl", ["$scope", "ComposantListe", function($scope, ComposantListe){
	$scope.messageWait = "Chargement des données ..."
	ComposantListe.rechercherListe().then(function(data) {
			$scope.listes = data;
			$scope.messageWait = "";
		}
	)
  	/**
  	 *
  	 */
	$scope.deleteListe = function(indexListe) {
		if (confirm("Etes-vous sûr de vouloir supprimer cette liste ?")) {
			ComposantListe.supprimerListe($scope.listes[indexListe]._id).then(
				function() {
					$scope.listes.splice(indexListe, 1);
				}
			)
		}
	}
	/**
  	 *
  	 */
	$scope.copyListe = function(indexListe) {
		if (confirm("Etes-vous sûr de vouloir copier cette liste ?")) {
			ComposantListe.copierListe($scope.listes[indexListe]._id).then(
				function(data) {
					$scope.listes.push(data);
				}
			)
		}
	}
  	/**
  	 *
  	 */
	$scope.modifyListe = function(indexListe) {
		location.hash="#/modificationListe/" + $scope.listes[indexListe]._id;
	}
	/**
  	 * Retour au menu principal
  	 */
	$scope.goHome = function() {
		location.hash = "#/listesEnCours";
	}	
}]);
/**
 * Contoleur de l'écran de listage de liste archivées
 */
modControleurs.controller("AffichageListesArchiveesCtrl", ["$scope", function($scope){
	$scope.listes = DB.listes;
}]);

/**
 * Contoleur du menu de navigation
 */
modControleurs.controller("MenuCtrl", ["$scope", "$location", function($scope, $location){
	$scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'menu';
        return page === currentRoute ? 'item-actif' : '';
    };        
}]);