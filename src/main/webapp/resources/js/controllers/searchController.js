angular
      .module('searchApp', [])
      .controller('searchController', function($scope, searchService){

          // Global variables available to the DOM
          $scope.term = '';
          $scope.results = [];
          $scope.loading = false;
          $scope.isFirstSearch = true;

          // Self explanatory
          $scope.search = function() {

              if($scope.isFirstSearch) {
                $('#search_area').animate({bottom: '15em'});
                $scope.isFirstSearch = false;
              }
              
              // Reset the current results and display loading wheel
              $scope.results = [];
              $scope.loading = true;

              // Call the search service to make the HTTP request
              searchService.search($scope.term.toLowerCase()).then(function(data) {

                if(data) {

                  // Push each result object to the results array
                  data.forEach(function(url) {
                    $scope.results.push(newSearchResult(url));
                  });

                  // For some reason the results come in backwards...
                  $scope.results.reverse();
                  for (var i = 0; i < $scope.results.length; i++) {
                    console.log($scope.results[i]);

                    if($scope.results[i].title.toLowerCase() == $scope.term) {

                      $scope.results.unshift($scope.results[i]);
                      $scope.results.splice(i+1, 1);
                      $scope.loading = false;
                      return;
                    }
                  }
                  $scope.loading = false;
                }
                else {

                  // No results :(
                  $scope.results = [];
                  $scope.loading = false;
                }

                

                // For testing purposes
                //console.log($scope.results);
              });
          }

          // Create a result object
          var newSearchResult = function(url) {
            var result = {};

            // Making the title readable
            var wikiTitle = decodeURIComponent(url.substr(30)).replace(/_/g," ");
            result.url = url;
            result.title = wikiTitle;
            return result;
          }
      });