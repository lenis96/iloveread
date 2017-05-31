app.controller('MainController', function($scope,$http) {
  $scope.title = 'Andres Lenis';

  $http.get("./rest/link")
  .then(function(response){
    $scope.links=response.data;
    console.log(response.data)
  });
});
