angular
      .module('searchApp')
      .service('searchService', function($http) {

          return({search: search});
          
          function search(term) {

              var request = $http({
                  method: 'get',
                  url: 'hello',
                  params: term
              });

              return(request.then(handleSuccess, handleError));
          }

          function handleError(response) {

              if( globalUtils.handleErrorResponse(response) ) return;
              
              if (!angular.isObject( response.data ) || !response.data.message) {

                  return($q.reject( "An unknown error occurred." ));
              }

              // Otherwise, use expected error message.
              return( $q.reject( response.data.message ) );
          }

          // The successful response is transformed, unwrapping the application data
          // from the API response payload.
          function handleSuccess( response, status ) {
              return( response.data );
          }
      });