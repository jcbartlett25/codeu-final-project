angular
      .module('searchApp', [])
      .controller('searchController', function($scope, searchService){

          $scope.term = '';
          $scope.urls = [];
          $scope.loading = false;

          $scope.search = function() {
              
              $scope.urls = [];
              $scope.loading = true;
              searchService.search($scope.term).then(function(data) {
                
                data.forEach(function(url) {
                    $scope.urls.push(url);
                });
                $scope.loading = false;
                console.log($scope.urls);
              });
          }
      });