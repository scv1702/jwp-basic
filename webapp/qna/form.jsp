<%@ page import="next.model.Question" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
    <%@ include file="/include/header.jspf" %>
</head>
<body>
<%@ include file="/include/navigation.jspf" %>

<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default content-main">
          <% Question question = (Question) request.getAttribute("question"); %>
          <% if (question == null) { %>
              <form name="question" method="post" action="/qna/create">
                  <div class="form-group">
                      <label for="create-title">제목</label>
                      <input type="text" class="form-control" id="create-title" name="title" placeholder="제목"/>
                  </div>
                  <div class="form-group">
                      <label for="create-contents">내용</label>
                      <textarea name="contents" id="create-contents" rows="5" class="form-control"></textarea>
                  </div>
                  <button type="submit" class="btn btn-success clearfix pull-right">질문하기</button>
              </form>
          <% } else { %>
              <form name="question" method="post" action="/qna/update">
                  <input type="hidden" id="questionId" name="questionId" value=${question.questionId}>
                  <div class="form-group">
                      <label for="update-title">제목</label>
                      <input type="text" class="form-control" id="update-title" name="title" value="${question.title}"/>
                  </div>
                  <div class="form-group">
                      <label for="update-contents">내용</label>
                      <textarea name="contents" id="update-contents" rows="5" class="form-control">${question.contents}</textarea>
                  </div>
                  <button type="submit" class="btn btn-success clearfix pull-right">질문하기</button>
              </form>
          <% } %>
          <div class="clearfix" />
        </div>
    </div>
</div>

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
</body>
</html>