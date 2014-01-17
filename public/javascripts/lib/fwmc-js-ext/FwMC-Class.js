var FwMC = {};
FwMC.Class = {
	/**
	 * Fabrique d'objets instanciables et "extensibles" à l'instar des classes Java
	 * @function {public string} ? 
	 * @return L'objet instanciable produit par la fabrique(une "classe")
	 * @param {string} properties - description litterale du prototype javascript de l'objet instanciable à créer ou à étendre. 
									Cette structure permet d'indiquer des modificateurs ECMASCript 5 sur les attributs
     *	Structure JSON d'un protoype :
	 * {
     * 	constructor: function(nom) {
     * 		this.nom = nom;
     *	},
     * direBonjour: {
	 *	value: function() {
	 * 		return "Bonjour " + this.nom + "!";
	 * 	},
	 *	configurable: false, // méthode non supprimable du prototype
	 *	writable: false, // méthode non surchargeable dans l'instance et non modifiable dans le prototype
	 *	enumerable: true
     * },
     *	attribut: {
     *		configurable: false, // attribut non supprimable du prototype
     *  	enumerable: false,
     *  	writable: true // attribut modifiable dans les instances
     *	}
	 * }
	 */
    extend : function (properties) {
    	// FwMC.Class si c'est le premier objet de la grappe sinon le prototype de l'objet que l'on est en train d'étendre
        var prototypeAEtendre = this.prototype || FwMC.Class; 
        // On utilise la fabrique d'objet proposée par ECMAScript 5 pour "cloner" le prototype à étendre
        var prototypeEtendu = Object.create(prototypeAEtendre); 
				
		// Enrichit le nouveau prototype à partir des élements passés en paramètre de la fonction extend de manière littérale
        Object.getOwnPropertyNames(properties).forEach(function(propName) { // Boucle sur les propriétés (clés de l'objet JS)
        	var descripteur = properties[propName]; // On récupère la valeur associée à la propriété courante de l'objet properties
        	if (descripteur instanceof Function) { // Si c'est une fonction, ce n'est pas un descripteur (si pas de descripteur utilisé pour cette entrée)
				descripteur = Object.getOwnPropertyDescriptor(properties, propName); // dans ce cas on extrait le descripteur de l'objet properties avec la méthode adhoc
            }
            Object.defineProperty(prototypeEtendu, propName, descripteur); // On définit la propriété dans le nouveau prototype
        });       
       
        // Création du constucteur de la nouvelle classe
		// Cette implémentation récupère directement le constructeur du prototype passé en paramètre (il faut donc l'appeler "constructor")
		// Cela nous évite de faire un call dessus, car dans ce cas c'est automatique !
        var constructeur = prototypeEtendu.constructor;
        if (!(constructeur instanceof Function)) {
            throw new Error("Le prototype de départ doit définir une fonction constructor");
        }
        
        // Finalisation du contructeur
        constructeur.prototype = prototypeEtendu; // on attache le nouveau prototype
        // On attache au champ super le prototype que l'on vient d'étendre (le parent donc) 
        // pour permettre d'effectuer de la sucharge (equivalent de super() en Java)
        constructeur._super = prototypeAEtendre; 
        // On attache la méthode extend au nouveau constructeur (ie. objet) pour qu'il soit lui aussi extensible
        constructeur.extend = this.extend; 
        
        // On retourne la "Classe" produite
        return constructeur;
    }
    
};