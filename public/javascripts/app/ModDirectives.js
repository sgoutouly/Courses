 /**
 * Module contenant les services utilitaires
 */
var toolBoxServices = angular.module("ToolBox.services", []);

.directive('myCustomer', function() {
      return {
        template: 'Name: {{customer.name}} Address: {{customer.address}}'
      };
    });