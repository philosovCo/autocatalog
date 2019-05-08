<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.domain.Auto" %>
<%@ page import="static org.apache.commons.lang3.StringUtils.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>AutoCatalog</title>
    <jsp:include page="bootstrap_css.jsp"/>
    <jsp:include page="bootstrap_scripts.jsp"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <% if (request.getParameter("name") != null) {%>
    <div class="row">
        <div class="col">
            <form class="mt-3" action="<%= request.getContextPath() %>">
                <input name="name" class="form-control" type="search" placeholder="Name"
                       value="<%= request.getParameter("name")!= null ? request.getParameter("name") : "" %>">
                <div class="row">
                    <div class="col-sm-4">
                        <label for="year">Year</label>
                        <input name="year" type="number" min="1800" max="2019" class="form-control"
                               id="year"
                               placeholder="Year"
                               value="<%=
                           request.getParameter("year")!= null ? request.getParameter("year") : ""
                           %>"
                        >
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="color">Color</label>
                            <select class="custom-select show-tick" name="color" id="color">
                                <%String color = request.getParameter("color"); %>
                                <%if (!isEmpty(color)) {%>
                                <option value="<%=color%>" selected
                                        hidden><%=color.substring(0, 1).toUpperCase() + color
                                        .substring(1)%>
                                </option>
                                <%} %>
                                <option></option>
                                <option value="white">White</option>
                                <option value="black">Black</option>
                                <option value="red">Red</option>
                                <option value="green">Green</option>
                                <option value="yellow">Yellow</option>
                            </select>

                        </div>
                    </div>
                    <div class="col-sm-4">
                        <label for="power">Power</label>
                        <input name="power" id="power" placeholder="Power" type="number" min="0"
                               max="1000"
                               class="form-control"
                               value="<%=
                           request.getParameter("power") != null ? request.getParameter("power") : ""
                           %>"
                        >
                    </div>
                </div>
                <button type="submit" class="btn btn-primary mt-2">Search</button>
            </form>
        </div>
    </div>
    <%}%>
    <div class="row">
        <% if (request.getAttribute("items") != null) { %>
        <% for (Auto item : (List<Auto>) request.getAttribute("items")) { %>
        <div class="col-sm-6 mt-3">
            <div class="card">
                <img src="<%= request.getContextPath() %>/images/<%= item.getPicture() %>"
                     class="card-img-top " style="max-height: 14.7rem; object-fit: cover;">
                <div class="card-body">
                    <h5 class="card-title"><%= item.getName() %>
                    </h5>
                    <p class="card-text"><%= item.getDescription()%>
                    </p>

                </div>
                <div class="card-body">
                    <a href="<%= request.getContextPath() %>/details/<%= item.getId() %>"
                       class="card-link">Details</a>
                    <a href="<%= request.getContextPath() %>/edit/<%= item.getId() %>"
                       class="card-link">Edit</a>
                    <a href="#"
                       data-href="<%= request.getContextPath() %>/delete/<%= item.getId() %>"
                       data-toggle="modal" data-target="#confirm-delete"
                       class="card-link text-danger">Remove</a>
                </div>
            </div>
        </div>
        <% } %>
        <% } %>
    </div>
</div>

<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                Car removal
            </div>
            <div class="modal-body">
                Are you sure about your decision?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <a class="btn btn-danger btn-ok">Yes</a>
            </div>
        </div>
    </div>
</div>

<script>
  $('#confirm-delete').on('show.bs.modal', function (e) {
    $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
  });
</script>
</body>
</html>