<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
	<%@ include file="/include/header.jspf" %>
</head>
<body>
	<script type="text/javascript">
		window.onload = function() {
			var errorMessage = "${errorMessage}";
			if (errorMessage) {
				alert(errorMessage);
			}
			window.history.back();
		}
	</script>
</body>
</html>