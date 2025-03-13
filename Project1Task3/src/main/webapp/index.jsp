<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Parks Searching</title>
</head>
<body>
<h1>U.S. National Parks</h1>
<h4>Created by Naim Sadeghian</h4>
<br/>
<h2>Parks</h2>
<p>Choose a Park</p>
<form action="search-park" method="GET">
    <select name='parks_code'>
        <option value='acad'>Acadia NP</option>
        <option value='cuva'>Cuyahoga Valley NP</option>
        <option value='grsm'>Great Smoky Mountains NP</option>
        <option value='maca'>Mammoth Cave NP</option>
        <option value='neri'>New River Gorge NP</option>
        <option value='shen'>Shenandoah NP</option>
    </select>
    <input type="submit" value="Submit" />
</form>
</body>
</html>