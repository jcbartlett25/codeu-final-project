<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Gradle + Spring MVC</title>

<!-- CSS Paths -->
<spring:url value="/resources/css/hello.css" var="coreCss" />
<spring:url value="/resources/vendor/bootstrap/css/bootstrap.min.css" var="bootstrapCss" />

<!-- JS Paths -->
<spring:url value="/resources/js/hello.js" var="coreJs" />
<spring:url value="/resources/vendor/bootstrap/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/vendor/angular/angular.min.js" var="angularJs" />
<spring:url value="/resources/vendor/jquery/jquery-1.11.1.min.js" var="jqueryJs" />

<!-- CSS Files -->
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<!-- JS Files -->
<script src="${jqueryJs}"></script>
<script src="${angularJs}"></script>
<script src="${bootstrapJs}"></script>
<script scr="${coreJs}"></script>

</head>
<body>
    <nav>
        <ul class="nav nav-pills">
            <li role="presentation"><a href="#">Google CodeU!</a></li>
        </ul>
    </nav>
    <div class="container">
        <h1 class="text-center">I wanna find...</h1><br>
    </div>
    <div class="container">
        <div class="col-lg-4 col-lg-offset-4">
            <div class="input-group">
                <input type="text" class="form-control" /><span class="input-group-btn">
                    <button class="btn btn-default" type="button">Go!</button>
                </span>
            </div>
        </div>
    </div>
</body>
</html>