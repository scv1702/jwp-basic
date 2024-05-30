// 질문 답변
$(".answerWrite input[type=submit]").click((e) => {
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type: 'post',
    url: '/api/qna/addAnswer',
    data: queryString,
    dataType: 'json',
    success: (answer, status) => {
      var answerTemplate = $("#answerTemplate").html();
      var template = answerTemplate.format(answer.writer.name, answer.createdDate, answer.contents, answer.answerId, answer.answerId);
      $(".qna-comment-slipp-articles").prepend(template);
    },
    error : (xhr, status) => {
      alert("error");
    }
  });
});

$(".qna-comment").on("click", ".form-delete", (e) => {
  e.preventDefault();

  var deleteBtn = $(this);
  var queryString = deleteBtn.closest("form").serialize();

  $.ajax({
    type: 'post',
    url: "/api/qna/deleteAnswer",
    data: queryString,
    dataType: 'json',
    error: (xhr, status) => {
      alert("error");
    },
    success: (json, status) => {
      var result = json.result;
      if (result.status) {
        deleteBtn.closest('article').remove();
      }
    }
  });
});

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};