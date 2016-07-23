angular
      .module('searchApp', [])
      .controller('searchController', function($scope, searchService){

          $scope.term = 'What are you looking for?';

          $scope.search = function() {
              
              searchService.search($scope.term);
          }
      });