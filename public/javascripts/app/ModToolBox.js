/**
 * Module contenant les services
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

/**
 * Module contenant les directives
 */
var toolBoxDirectives = angular.module("ToolBox.directives", []);

/**
 * Directive de menu latéral gauche
 */
toolBoxDirectives.directive('tbMenuLeft', function() {

    return {
    	link : function($scope, $element, attrs) {
			var menuWidth = $element.prop("offsetWidth"), 
				initialMouseX = 0, 
				clientX = 0;

			// Initialisation des élément du DOM
	    	bouton = document.getElementById('menuBouton');
 			if(bouton) {angular.element(bouton).on('click', toggleMenu);}	
	    	$element.on("touchstart", startDragMenu);
	    	$element.on("click", hideMenu);
	    	$shadow = angular.element("<div class='menu-shadow'></div>");
	    	$shadow.on('click', hideMenu); 
	    	$element.parent().append($shadow);

	    	/* Au démarrage du glissé */
			function startDragMenu(e) {
				$element.removeClass("animate");
				initialMouseX = getMousePosition(e).X;		
				initialleft = $element.hasClass('show') ? 0 : -(menuWidth);
				if (initialMouseX < 30 + initialleft + menuWidth) {
					$element.on("touchmove", dragMenu);
					$element.on("touchend", releaseMenu);
				}
			}

			/* Pendant le glissé */
			function dragMenu(e) {
				e.stopImmediatePropagation();
				e.preventDefault();
				clientX = getMousePosition(e).X;
				initialleft = $element.hasClass('show') ? 0 : -(menuWidth);
				// On calcule la position relative
				var position = clientX + initialleft - initialMouseX;
				if (position > 0) {
					position = 0;
				} else if (position < menuWidth * -1 ) {
					position = menuWidth * -1;
				}
				$element.prop('style').webkitTransform = "translateX(" + position  + "px)";

			}
			
			/* Fin du glissé */
			function releaseMenu(e) {
				// On calcule la position relative
				var position = clientX + initialleft - initialMouseX ;
				if (position > Math.round(menuWidth / 2) * -1) {
					$element.prop('style').webkitTransform = "";
					$element.addClass("animate");
					$element.addClass("show");
			        $shadow.addClass("show");
			        reset();
				} else {
					hideMenu(e);
				}
			}
			
			/* bascule de ouvert vers fermé et vice-versa */
			function toggleMenu(e) {
				e.preventDefault();
				$element.addClass("animate");
				$element.toggleClass("show");
				$shadow.toggleClass("show");
				reset();
			}
			
			/*  Permet de cacher le menu */
			function hideMenu(e) {
				$element.prop('style').webkitTransform = "";
				$element.addClass("animate");
				$element.removeClass("show");
				$shadow.removeClass("show");
				reset();
			}

			/* Reset la gestion des evenements du menu  */
			function reset() {
				$element.off("touchmove");
				$element.off("touchend");
			}

			/* Permet de recuperer la position de la souris par rappor a l'evenement */
			function getMousePosition(e) {
				var position = {};
				var evt = e || window.event;
				var touches;
				if (e.touches) {
					touches = e.touches;
				} else if(e.originalEvent) {
					touches = evt.originalEvent.changedTouches;
				}
				if (touches) {
					position.X = touches[0].clientX;
					position.Y = touches[0].clientX;
				} else {
					position.X = evt.clientX;
					position.Y = evt.clientX;
				}
				return position;
			}

	    }
	}

});