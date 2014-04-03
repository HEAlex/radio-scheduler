<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Радио СумДУ</title>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.min.css">
</head>
<body>
<div class="container">
    <div class="modal" style="top: 300px;">
        <div class="modal-header">
            <h3>Авторизация</h3>
        </div>
        <form class="form-vertical" action="/login" method="post" style="margin: 0;">
            <div class="modal-body">
                <div class="row">
                    <div class="span5 control-group<c:if test="${param.error!=null}"> error</c:if>">
                        <label for="login">Имя пользователя</label>
                        <input class="span4 input-large" id="login" type="text" name="login">
                        <label for="password">Пароль</label>
                        <input class="span4 input-large" id="password" type="password" name="password">
                    </div>
                </div>
                <c:if test="${param.error!=null}">
                    <div class="alert alert-error">
                        <strong>Имя пользователя и пароль не совпадают.</strong>
                    </div>
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary"><i class="icon-home icon-white"></i> Войти</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>