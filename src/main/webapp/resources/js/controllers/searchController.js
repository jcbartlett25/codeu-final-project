angular
      .module('searchApp', [])
      .controller('searchController', function($scope, searchService){

          $scope.term = '';

          $scope.search = function() {
              
              searchService.search($scope.term);
          }
      });