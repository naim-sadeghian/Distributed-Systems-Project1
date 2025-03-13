<%--
  Created by IntelliJ IDEA.
  User: Naim
  Date: 2/8/2025
  Time: 5:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String[] weather = (String[]) request.getAttribute("weather");
    java.util.List<String> activities = (java.util.List<String>) request.getAttribute("activites");
%>
<html>
<head>
    <title>Park Info</title>
</head>
<body>
<h1>${parkName} National Park</h1>
<!-- Display Image -->
<img src="${imageURL}" alt="Park Image" width="1024" />

<!-- Display Weather Info -->
<h3>Current conditions:</h3>
<ul>
    <% if (weather != null) {
        for (String condition : weather) { %>
    <li><%= condition %></li>
    <% } } %>
</ul>

<!-- Display Activities -->
<h3>${parkName} Activities:</h3>
<ul>
    <% if (activities != null) {
        for (String activity : activities) { %>
    <li><%= activity %></li>
    <% } } %>
</ul>
</body>
</html>
