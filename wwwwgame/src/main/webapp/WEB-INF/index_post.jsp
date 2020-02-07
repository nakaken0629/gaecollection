<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%-- JSTLの利用の宣言 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>イツドコデダレガナニシタ</title>
<link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h1>イツドコデダレガナニシタ</h1>
<div class="box">
<%-- JSTLでリクエスト属性の値を表示する --%>
<p>
<c:out value="${sentence}" />
</p>
<p>
<a href="/index">もう一度文章を作成する</a>
</p>
</div>
</body>
</html>