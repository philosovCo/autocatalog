<%
    StringBuilder builder = new StringBuilder();
    request.getParameterMap().forEach((s, strings) ->
            builder.append(builder.length() == 0 ? "?" : "&")
                    .append(s).append("=")
                    .append(strings != null && strings.length == 1 ? strings[0] : ""));
%>

<div class="container">
    <div class="row">
        <div class="col">
            <nav class="navbar navbar-expand-lg navbar-light bg-light justify-content-between">
                <a class="navbar-brand" href="<%= request.getContextPath() + "/"%>">
                    <img src="https://img.icons8.com/clouds/100/000000/fiat-500.png" width="30" height="30"
                         class="d-inline-block align-top" alt="">
                    Auto Catalog
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse " id="navbarSupportedContent">

                    <% if (request.getAttribute("items") != null) { %>
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a href="<%= request.getContextPath() %>/new"
                               class="nav-link">New Auto</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                               data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Import/Export
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a href="#" data-toggle="modal" data-target="#upload-csv" class="dropdown-item">Import
                                    CSV</a>
                                <a class="dropdown-item" href="<%= request.getContextPath() %>/csv">Export
                                    CSV <%if (builder.length() != 0) {%>all rows<%}%></a>
                                <%if (builder.length() != 0) {%>
                                <a class="dropdown-item"
                                   href="<%= request.getContextPath() %>/csv<%=builder.toString()%>">Export CSV by
                                    filters</a>
                                <%}%>
                            </div>
                        </li>
                    </ul>
                    <%}%>
                    <%if (request.getParameter("name") == null) {%>
                    <form class="form-inline ml-md-auto" action="<%= request.getContextPath() %>">
                        <input name="name" class="form-control" type="search" placeholder="Search">
                    </form>
                    <%}%>
                </div>
            </nav>
        </div>

        <div class="modal fade" id="upload-csv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        Import auto from CSV file
                    </div>
                    <div class="modal-body">
                        <form class="mt-3" action="<%= request.getContextPath() %>/csv" method="post" enctype="multipart/form-data">
                            <div class="custom-file">
                                <input type="file" class="custom-file-input" id="file_csv"
                                       name="file_csv" accept=".csv" required>
                                <label class="custom-file-label" for="file_csv">Choose file...</label>
                            </div>
                            <button type="submit" class="btn btn-primary mt-2">Import
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

