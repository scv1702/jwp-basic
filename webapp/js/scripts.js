// 질문 답변
$(".answerWrite input[type=submit]").click(function (e){
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type: 'post',
    url: '/api/qna/addAnswer',
    data: queryString,
    dataType: 'json',
    success: (json, status) => {
      var answer = json.data;
      alert(json.message);
      var answerTemplate = $("#answerTemplate").html();
      var template = answerTemplate.format(answer.writer.name, answer.createdDate, answer.contents, answer.answerId, answer.answerId);
      $(".qna-comment-slipp-articles").prepend(template);
      $(".qna-comment-count strong").text(answer.question.countOfAnswer);
    },
    error : (xhr, status) => {
      var json = xhr.responseJSON;
      alert(json.message);
    }
  });
});

$(".qna-comment").on("click", ".form-delete", function (e) {
  e.preventDefault();

  var deleteBtn = $(this);
  var queryString = deleteBtn.closest("form").serialize();

  $.ajax({
    type: 'post',
    url: "/api/qna/deleteAnswer",
    data: queryString,
    dataType: 'json',
    error: (xhr, status) => {
      var json = xhr.responseJSON;
      alert(json.message);
    },
    success: (json, status) => {
      alert(json.message);
      deleteBtn.closest('article').remove();
      //TODO: 동시에 여러명이 삭제하면 유효하지 않음
      var countOfAnswer = parseInt($(".qna-comment-count strong").text());
      $(".qna-comment-count strong").text(countOfAnswer - 1);
    }
  });
});

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, (match, number) => {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};