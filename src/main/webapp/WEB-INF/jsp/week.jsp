<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table id="days" class="table table-striped table-bordered table-condensed"
       startDate="<fmt:formatDate pattern="dd.MM.yy" value="${startDate}"/>"
       endDate="<fmt:formatDate pattern="dd.MM.yy" value="${endDate}"/>">
    <tr>
        <c:forEach items="${days}" var="day">
            <th class="day-title">${day.name} <fmt:formatDate pattern="dd.MM.yy" value="${day.date}"/></th>
        </c:forEach>
    </tr>
    <c:forEach var="timePart" begin="0" end="8" step="1">
        <tr>
            <c:forEach var="day" begin="0" end="6" step="1">
                <td day="${day}"
                    date="<fmt:formatDate pattern="dd.MM.yy" value="${days[day].date}" />"
                    timePart="${timePart}">
                    <h6 class="title">
                        <i class="icon-time"></i>${breaks[timePart]}<span class="totalTime">00:00</span>
                    </h6>
                    <ul class="">
                        <c:forEach var="track" items="${tracks[day][timePart]}">
                            <li fileId="${track.fileId}"
                                trackid="${track.id}"
                                duration="${files[track.fileId].duration}">
                                <span>${files[track.fileId].shortName}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>