<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>WikiSearch</title>

<!-- CSS Paths -->
<spring:url value="/resources/css/main.css" var="coreCss" />
<spring:url value="/resources/vendor/bootstrap/css/bootstrap.min.css" var="bootstrapCss" />

<!-- JS Paths -->
<spring:url value="/resources/vendor/bootstrap/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/vendor/angular/angular.min.js" var="angularJs" />
<spring:url value="/resources/vendor/jquery/jquery-1.11.1.min.js" var="jqueryJs" />
<spring:url value="/resources/js/searchApp.js" var="coreJs" />

<!-- CSS Files -->
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<!-- JS Files -->
<script src="${jqueryJs}"></script>
<script src="${angularJs}"></script>
<script src="${bootstrapJs}"></script>
<script scr="${coreJs}"></script>
<script src="/resources/js/controllers/searchController.js"></script>
<script src="/resources/js/services/searchService.js"></script>
<script src="/resources/js/search.js"></script>

</head>
<body ng-app="searchApp" ng-controller="searchController">
    <nav>
        <ul class="nav nav-pills">
        </ul>
    </nav>
    <div id="search_area">
    <div class="container">
        <h1 class="text-center" id="search_title">WikiSearch</h1><br>
    </div>
    <div class="container">
        <div class="col-lg-4 col-lg-offset-4">
            <div class="input-group">
                <input type="text" id="search_field" type="text" placeholder="What are you looking for?" ng-model="term" class="form-control" />
                <span class="input-group-btn">
                    <button class="btn btn-default" ng-click="search()" id="search_button" ng-submit="search()" type="button">Go!</button>
                </span>
            </div>
        </div>
    </div>
    <div>
        <img class="center-block" ng-show="loading" src="/resources/img/loading.gif" height="30px"/>
    <div>
    <div id="container">
        <div class="text-center container" id="search_results">
            <div style="color:white;" ng-repeat="result in results"><a ng-href="{{result.url}}">{{result.title}}</a></div>
        </div>
    </div>
    </div>
</body>
</html>