angular
      .module('searchApp')
      .service('searchService', function($http) {

          return({search: search});
          
          function search(term) {

              var param = {'term': term};
              var request = $http({
                  method: 'get',
                  url: '/search/api/search',
                  params: param
              });

              return(request.then(handleSuccess, handleError));
          }

          function handleError(response) {
              
              return response.data;
          }

          // The successful response is transformed, unwrapping the application data
          // from the API response payload.
          function handleSuccess( response, status ) {
              return( response.data );
          }
      });