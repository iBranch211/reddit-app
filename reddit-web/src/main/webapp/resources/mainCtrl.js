var app = angular.module('myApp', ["ngTable","ngResource","ngDialog"]);
app.config(['$httpProvider', function ($httpProvider) {
	$httpProvider.interceptors.push(function ($q,$rootScope) {
	    return {
	        'responseError': function (responseError) {
	        	$rootScope.loading = false;
	            $rootScope.message = responseError.data.message;
	            return $q.reject(responseError);
	        }
	    };
	});
}]);

app.controller('mainCtrl', function($scope,NgTableParams,$resource,ngDialog,$rootScope) {
	$rootScope.loading = false;
	$scope.feed = {name:"New feed", url: ""};
    $scope.feeds = $resource("api/myFeeds/:feedId",{feedId:'@id'},{
        'update': { method:'PUT' }
    });
    
    $scope.tableParams = new NgTableParams({}, {
      getData: function(params) {
    	  var queryParams = {page:params.page()-1 , size:params.count()};
    	  var sortingProp = Object.keys(params.sorting());
          if(sortingProp.length == 1){
        	  queryParams["sort"] = sortingProp[0];
        	  queryParams["sortDir"] = params.sorting()[sortingProp[0]];
          }
        return $scope.feeds.query(queryParams, function(data, headers) {
          var totalRecords = headers("PAGING_INFO").split(",")[0].split("=")[1];
          params.total(totalRecords);
          console.log(params.total());
          return data;
        }).$promise;
      }
    });
    
	$scope.confirmDelete = function(id){
		if (confirm("Do you really want to delete this site?") == true) {
			$scope.feeds.delete({feedId:id}, function(){
				$scope.tableParams.reload();
			});
			
    	} 
	}
	
	$scope.addNewFeed = function(){
		$scope.feed = {name:"New Feed", url: ""};
		ngDialog.open({ template: 'templateId',	scope: $scope, className: 'ngdialog-theme-default' });
	}
	
	$scope.editFeed = function(row){
		$scope.feed.id = row.id;
		$scope.feed.name = row.name;
		$scope.feed.url = row.url;
		
		ngDialog.open({ template: 'templateId',	scope: $scope, className: 'ngdialog-theme-default' });
	}
    
	$scope.save = function(){
		ngDialog.close('ngdialog1');
		$rootScope.loading = true;
		if(! $scope.feed.id){
			$scope.createFeed();
		}else{
			$scope.updateFeed();
		}
	}
	
	$scope.createFeed = function(){
		$scope.feeds.save($scope.feed, function(){
			$scope.tableParams.reload();
			$rootScope.loading = false;
		});
	}
	
	$scope.updateFeed = function(){
		$scope.feeds.update($scope.feed, function(){
			$scope.tableParams.reload();
			$rootScope.loading = false;
		});
	}
	
	

    
});