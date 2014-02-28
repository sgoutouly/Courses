/**
 * Module contenant les services du chat websocket
 */
var modChatServices = angular.module("chat.services", []);

/**
 * Les services ...
 * 
 */
modChatServices.factory("ComposantChat", ["$q", "$rootScope", function($q, $rootScope) {

	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	var socket = null;

	var ComposantChat = {};
    ComposantChat.connect = function() {
    	socket = new WS("ws://" + location.host + "/chat/");   
	};

	/* 
	 * la fonction onmessage du composant permet d'enregistrer le callback de mise à jour de l'IHM
	 * Le principe est de cabler sur le onmessage de la socket, l'appel du callback et de forcer le 
	 * "dirty check" de angular suite à l'exécution du callback en l'ancapsulant dans $rootScope.$apply()
	 */
	ComposantChat.onmessage = function (callback) {
      socket.onmessage = function (event) {  
        $rootScope.$apply(function () {
          callback.apply(socket, [JSON.parse(event.data)]); // appel du callback en lui passant la socket comme contexte et les data en argment unique
        })
      };
    };

    /** */
	ComposantChat.disconnect = function() {
		socket.close();
	};

	ComposantChat.send = function(message) {
		socket.send(JSON.stringify(message));
	};

   return ComposantChat;
}]);


/**
 * Module contenant les controleurs de Chat
 */
var modChatControleurs = angular.module("chat.controleurs", []);

/**
 * Contoleur de l'écran de création d'un chat
 * @utilise le service ...
 */
modChatControleurs.controller("OpenChatCtrl", ["$scope", "ComposantChat", 
	function($scope, ComposantChat){

	$scope.message;
	$scope.messages = [];

	ComposantChat.connect();
	ComposantChat.onmessage(
		function(data) {
	 		$scope.messages.push(data);
		}
	);

	$scope.sendMessage = function(event) {
		if(event && event.keyCode !== 13) {return;} 
		else {
			if(event) {event.preventDefault();}
			ComposantChat.send({"text": $scope.message});
			$scope.message = "";
		}
	}

	$scope.disconnect = function() {
		ComposantChat.disconnect();
	}

}]);

/**
 * Contrôleur de l'écran permettant de rejoindre un chat
 * @utilise le service ...
 */
modChatControleurs.controller("JoinChatCtrl", ["$scope", "ComposantChat", 
	function($scope, ComposantChat){
	
	/* Enrichissement du scope */
	$scope.formListeId = $routeParams.listeId;
	$scope.messageWait = "Chargement des données ...";

	ComposantChat.consulterListe($scope.formListeId).then(
		function(data) {
			$scope.dateRedaction = data.dateRedaction;
			$scope.formCourses = data.courses;
		}
	);	 	

}]);

/* 
 * Routage de ces contrôleurs
 */	
coursesApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/openChat', {
    				templateUrl: 'partials/chat.html',
    				controller: 'OpenChatCtrl'
  				}).
  				when('/joinChat', {
    				templateUrl: 'partials/chat.html',
    				controller: 'JoinChatCtrl'
  				})
		}
])