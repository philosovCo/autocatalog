<%@ page import="ru.itpark.domain.Auto" %>
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
        <% if (request.getAttribute("item") != null) { %>
        <% Auto item = (Auto) request.getAttribute("item"); %>
        <div class="col-sm-6 mt-3 mx-auto" >
            <div class="card">
                <img src="<%= request.getContextPath() %>/images/<%= item.getPicture() %>" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title"><%= item.getName() %></h5>
                    <p class="card-text"><%= item.getDescription()%></p>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">Color: <%= item.getColor()%></li>
                    <li class="list-group-item">Power: <%= item.getPower()%></li>
                    <li class="list-group-item">Year: <%= item.getYear()%></li>
                    <li class="list-group-item">Transmission: <%= item.getTransmission()%></li>
                </ul>
            </div>
        </div>
        <% } %>
    </div>
</div>


</body>
</html>