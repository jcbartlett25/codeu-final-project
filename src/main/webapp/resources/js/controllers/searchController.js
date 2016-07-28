angular
      .module('searchApp', [])
      .controller('searchController', function($scope, searchService){

          // Global variables available to the DOM
          $scope.term = '';
          $scope.results = [];
          $scope.loading = false;

          // Self explanatory
          $scope.search = function() {
              
              // Reset the current results and display loading wheel
              $scope.results = [];
              $scope.loading = true;

              // Call the search service to make the HTTP request
              searchService.search($scope.term).then(function(data) {

                if(data) {

                  // Push each result object to the results array
                  data.forEach(function(url) {
                    $scope.results.push(newSearchResult(url));
                  });

                  // For some reason the results come in backwards...
                  $scope.results.reverse();
                  $scope.loading = false;
                }
                else {

                  // No results :(
                  $scope.results = [];
                  $scope.loading = false;
                }

                // For testing purposes
                console.log($scope.results);
              });
          }

          // Create a result object
          var newSearchResult = function(url) {
            var result = {};

            // Making the title readable
            var wikiTitle = url.substr(30).replace(/_/g," ").replace(/%26/g,"&").replace(/%27/g,"'");
            result.url = url;
            result.title = wikiTitle;
            return result;
          }
      });