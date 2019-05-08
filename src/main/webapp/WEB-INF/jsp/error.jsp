<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <jsp:include page="bootstrap_css.jsp"/>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <h1>Oops you've have encountered an error</h1>
        <div class="col-sm-6 mt-3 mx-auto">
            <div class="card">
                <blockquote class="blockquote mx-auto">
                    <p class="mb-0">
                        Oops you've have encountered an error
                    </p>
                    <footer class="blockquote-footer">
                        Please try again <cite>later</cite>
                    </footer>
                </blockquote>
            </div>
        </div>
    </div>
</div>


</body>
</html>