/**
 * Objet de gestion du menu principal
 * @param menu Element du menu
 * @param bouton Bouton de toggle
 * @param shadow Zone d'ombrage
 * @returns
 */
function MenuPrincipal(menu, bouton, shadow) {
	/* attribut prive menu */
	this.menu = menu;
	/* attribut prive bouton */
	this.menuButton = bouton;
	/** attribut prive ombrage */
    this.shadow = shadow;
    var that = this;
	menu.addEventListener("touchstart", this, false);
	if (bouton) {bouton.addEventListener('click', this, false);}
	shadow.addEventListener('click', this, false);
	
	/**
	 * Methode publique
	 * Gestion des evenements
	 */
	this.handleEvent = function(event) {
		switch (event.type) {
			case 'touchstart': startDragMenu(event); break;
			case 'touchmove': dragMenu(event); break;
			case 'touchend': releaseMenu(event); break;
			case 'click': 
				if (event.target == this.menuButton) {
					return toggleMenu(event); break;
				} else if (event.target == this.shadow) {
					return this.hideMenu(event);break;
				}
		}
	}
	
	/**
	 * Methode privee
	 * Demarrage du drag
	 */
	function startDragMenu(e) {
		var $menu = $(that.menu);
		$menu.removeClass("animate");
		//e.preventDefault();
		that.initialMouseX = getMousePosition(e).X;
		that.initialleft = $menu.position().left;
		if (that.initialMouseX < 30 + that.initialleft + $menu.width()) {
			window.addEventListener("touchmove", that, false);
			window.addEventListener("touchend", that, false);
		}
	}
	
	/**
	 * Reset la gestion des evenements du menu
	 */
	function reset() {
		window.removeEventListener("touchmove", that, false);
		window.removeEventListener("touchend", that, false);
	}
	
	/**
	 * Methode privee
	 * Deplacement du menu
	 */
	function dragMenu(e) {
		var $menu = $(that.menu);
		e.stopImmediatePropagation();
		e.preventDefault();
		that.clientX = getMousePosition(e).X;
		// On calcule la position relative
		var position = that.clientX + that.initialleft - that.initialMouseX ;
		if (position > 0) {
			position = 0;
		} else if (position < $menu.width() * -1 ) {
			postion = $menu.width() * -1;
		}
		document.getElementById("menu").style.webkitTransform = "translateX(" + position  + "px)";
		// Permet d'eviter de masquer le menu s'il vient d'être affiche
		if (e.target == that.menuButton) {
			that.ignoreClick = true;
		} else {
			that.ignoreClick = false;
		}
	}
	
	/**
	 * Methode privee
	 * Fin du deplacement
	 * @param e
	 */
	function releaseMenu(e) {
		var $menu = $(that.menu);
		var $shadow = $(that.shadow);
		// On calcule la position relative
		var position = that.clientX + that.initialleft - that.initialMouseX ;
		$menu.addClass("animate");
		if ($menu.position().left > Math.round($menu.width() / 2) * -1) {
			document.getElementById("menu").style.webkitTransform = "";
			$menu.addClass("show");
	        $shadow.addClass('show');
		} else {
			document.getElementById("menu").style.webkitTransform = "";
			$menu.removeClass("show");
	        $shadow.removeClass('show');
		}
		reset();
	}
	
	/**
	 * Methode privee
	 * Toggle du menu
	 * @param e
	 */
	function toggleMenu(e) {
		e.preventDefault();
		var $menu = $(that.menu);
		var $shadow = $(that.shadow);
		if (!that.ignoreClick) {
			document.getElementById("menu").style.webkitTransform = "";
			$menu.addClass('animate');
			$menu.toggleClass('show');
			$shadow.toggleClass('show');
		} else {
			that.ignoreClick = false;
		}
		reset();
	}
	
	/**
	 * Methode publique
	 * Permet de cacher le menu
	 */
	this.hideMenu = function(e) {
		var $menu = $(that.menu);
		var $shadow = $(that.shadow);
		document.getElementById("menu").style.webkitTransform = "";
		$menu.addClass('animate');
		$menu.removeClass('show');
		$shadow.removeClass('show');
		reset();
	}
	
	/**
	 * Methode privee
	 * Permet de recuperer la position de la souris par rappor a l'evenement
	 * @param e
	 * @returns position
	 */
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

$(document).ready(function() {
		var menuPrincipal = new MenuPrincipal(
		document.getElementById("menu"), 
		document.getElementById("menuButton"), 
		document.getElementById("shadow"));
	$("#menu li").on("click", function(e) {menuPrincipal.hideMenu(e)});
});