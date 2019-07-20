<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.
org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>イツドコデダレガナニシタ</title>

<!--スタイルシートの追加 -->
<link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>

<!-- <form>タグの準備 -->
<form action="/index" method="post">
<h1>イツドコデダレガナニシタ</h1>
<!-- 入力項目の準備 -->
<div class="box">
  <dl>
    <dt>いつ</dt>
    <dd><input type="text" name="when" /></dd>
    <dt>どこで</dt>
    <dd><input type="text" name="where" /></dd>
    <dt>だれが</dt>
    <dd><input type="text" name="who" /></dd>
    <dt>なにした</dt>
    <dd><input type="text" name="what" /></dd>
  </dl>
  <p><input type="submit" value="新しい文章を作成する" /></p>
</div>
</form>
</body>
</html>