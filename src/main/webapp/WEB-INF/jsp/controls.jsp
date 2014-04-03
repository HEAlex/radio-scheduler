<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setCharacterEncoding("UTF-8");
    request.setCharacterEncoding("UTF-8");
%>
<html>
<head>
    <title>Радио СумДУ</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.min.css">
    <script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.17.custom.min.js"></script>
    <script type="text/javascript" src="/js/jstree/jquery.jstree.js"></script>
    <script type="text/javascript" src="/js/jquery.jplayer.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap-modal.js"></script>
    <script type="text/javascript" src="/js/control.js"></script>
</head>
<body>

<div id="fileuploader-modal" class="modal hide fade" style="display: none; ">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">×</a>

        <h3>Загрузка файлов</h3>
    </div>
    <div class="modal-body">
        <div id="uploader-container">
            <h4>Выберите файлы</h4>

            <div id="filelist"></div>
            <br/>
        </div>
    </div>
    <div class="modal-footer">
        <a style="float: left;" class="btn btn-success" id="pickfiles" href="#">Выбрать файлы</a>
        <a style="float: left;" class="btn btn-primary" id="uploadfiles" href="#">Начать загрузку</a>
        <a href="#" class="btn" data-dismiss="modal">Закрыть</a>
    </div>
</div>

<div id="wrapper">
    <div id="files-container" style="border-top-width: 0; border-bottom-width: 0;">
        <div>
            <button id="refreshButton" class="btn btn-small btn-primary" style="float: right;">
                <i class="icon-refresh icon-white"></i>
                Синхронизировать
            </button>
            <h3>Файлы</h3>
        </div>
        <div id="files"></div>
    </div>
    <div id="calendar">
        <!-- Top controls -->
        <div id="weekControls" class="row-fluid">
            <div class="span12" style="text-align: center;">
                <button class="btn btn-primary" id="prevWeek">
                    <i class="icon-arrow-left icon-white"></i>&nbsp;
                </button>
                <button class="btn btn-primary" id="curWeek">
                    <i class="icon-home icon-white"></i>&nbsp;
                </button>
                <h3 id="datePanel" style="width: 250px; display: inline-block;"><fmt:formatDate pattern="dd.MM.yy" value="${startDate}"/> &mdash;
                    <fmt:formatDate pattern="dd.MM.yy" value="${endDate}"/></h3>
                <button class="btn btn-primary" id="nextWeek">
                    <i class="icon-arrow-right icon-white"></i>&nbsp;
                </button>
            </div>
        </div>
        <!-- Calendar controls -->
        <div id="week">
            <jsp:include page="week.jsp"/>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/plupload/plupload.full.js"></script>
</body>
</html>