<%@ page import="ru.itpark.domain.Auto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>

    <jsp:include page="bootstrap_css.jsp"/>
    <jsp:include page="bootstrap_scripts.jsp"/>
</head>
<body>
<%
    Auto auto = (Auto) request.getAttribute("item");
    String url = auto == null ? "new" : "edit/" + auto.getId();
%>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-md-6 mx-auto">
            <form class="mt-3" action="<%= request.getContextPath() %>/<%= url %>" method="post"
                  enctype="multipart/form-data">
                <%if (auto != null) {%>
                <div class="form-group">
                    <input type="text" hidden name="id"
                           value="<%=auto.getId()%>">
                </div>
                <%}%>

                <div class="form-group">
                    <label for="name">Name</label>
                    <input name="name" type="text" class="form-control"
                           value="<%= auto != null && auto.getName() != null ? auto.getName() : ""%>"
                           id="name" placeholder="Name" required>
                </div>
                <div class="form-group">
                    <label for="color">Color</label>
                    <select class="custom-select show-tick" name="color" id="color" required>
                        <%if (auto != null && auto.getColor() != null) {%>
                        <%String color = auto.getColor(); %>
                        <option value="<%=color%>" selected
                                hidden><%=color.substring(0, 1).toUpperCase() + color.substring(1)%>
                        </option>
                        <%} %>
                        <option value="white">White</option>
                        <option value="black">Black</option>
                        <option value="red">Red</option>
                        <option value="green">Green</option>
                        <option value="yellow">Yellow</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="year">Year</label>
                    <input name="year" type="number" min="1800" max="2019" class="form-control" id="year"
                           placeholder="Year"
                           required
                           value="<%= auto != null && auto.getYear() != null ? auto.getYear() : ""%>"
                    >
                </div>

                <div class="form-group">
                    <label for="power">Power</label>
                    <input name="power" id="power" placeholder="Power" type="number" min="0" max="1000"
                           class="form-control"
                           required
                           value="<%= auto != null && auto.getPower() != null ? auto.getPower() : ""%>"
                    >
                </div>

                <div class="form-group">
                    <label for="transmission">Transmission</label>
                    <select class="custom-select show-tick" name="transmission" id="transmission" required>
                        <%if (auto != null && auto.getTransmission() != null) {%>
                        <%String transmission = auto.getTransmission(); %>
                        <option value="<%=transmission%>" selected
                                hidden><%=transmission.substring(0, 1).toUpperCase() + transmission.substring(1)%>
                        </option>
                        <%} %>
                        <option value="auto">Auto</option>
                        <option value="manual">Manual</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea name="description" class="form-control" id="description" placeholder="Description"
                              required><%= auto != null && auto.getDescription() != null ? auto.getDescription() : ""%></textarea>
                </div>

                <div class="custom-file">
                    <input type="file" class="custom-file-input" id="file" name="file" accept="image/*" required>
                    <label class="custom-file-label" for="file">Choose file...</label>
                </div>

                <button type="submit" class="btn btn-primary mt-2"><%=auto == null ? "Create" : "Update"%>
                </button>
            </form>
        </div>
    </div>
</div>
</body>
</html>