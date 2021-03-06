/**
 * Assemblage des modules controleurs et services
 */
var coursesApp = angular.module("courses", [
	"ngRoute", 
	"ngAnimate", 
	"ToolBox.directives", 
  	"ToolBox.services", 
	"courses.errors", 
	"courses.services", 
	"courses.controleurs",
  	"chat.services",
  	"chat.controleurs"
]);

/**
 * Gestion des routes globales
 * Les routes associées à des contrôleurs sont définies avec
 * les contrôleurs concernés
 */
// coursesApp.config(['$routeProvider',
//   function($routeProvider) {
//     $routeProvider.
//       when('/menu', {
//         templateUrl: 'partials/menu.html'
//       })
//   }]);




