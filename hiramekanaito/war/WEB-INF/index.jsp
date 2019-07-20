<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>全自動クイズ ヒラメかナイト</title>
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<script type="text/javascript"
  src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
<script type="text/javascript" src="/js/hiramekanaito.js"></script>
</head>
<body>
<!-- テレビ画面を表示するパネル -->
<div id="tv">
  <!-- ヒントを表示するテーブル -->
  <table id="hints">
    <tr>
      <c:forEach var="hint" varStatus="status" items="${answer.hints}">
        <td id='<c:out value="${status.index}" />_close' class="close"><c:out
            value="${status.index + 1}" /></td>
        <td id='<c:out value="${status.index}" />_open' class="open"><c:out
            value="${hint}" /></td>
        <c:if test="${status.index != 15 && status.index % 4 == 3}">
    </tr>
    <tr>
      </c:if>
      </c:forEach>
    </tr>
  </table>
  <!-- 答えを表示するテーブル -->
  <table id="answer">
    <tr>
      <td><c:out value="${answer.text}" /></td>
    </tr>
  </table>
</div>

<!-- 司会を表示するパネル -->
<div id="host"><img src="/img/hirame_mc.jpg" /></div>
<div id="bar">&nbsp;<img src="/img/hirame_bar.gif" /></div>

<!-- 各種ボタンやテキストボックスが表示されるパネル -->
<div id="panel">
  <p><input type="text" id="answerinput" /> <input type="button"
    id="answerbutton" value="答える" /> <span id="message"></span></p>
  <p><input type="button" id="nextbutton" value="次の問題" /> <input
    type="button" id="twitterbutton" value="twitterでつぶやく" /><input
    type="hidden" id="id" value='<c:out value="${answer.id}" />'></input>
  <input type="hidden" id="answertext"
    value='<c:out value="${answer.text}" />'></input><input
    type="hidden" id="answerfurigana"
    value='<c:out value="${answer.furigana}" />'></input></p>
</div>
</body>
</html>
