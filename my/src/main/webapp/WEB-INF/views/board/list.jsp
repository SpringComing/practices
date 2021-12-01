<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.request.contextPath }/board" method="get">
					<input type="text" id="kwd" name="kwd" value="${kwd }"> 
					<input type="submit" value="찾기">
				</form>

				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th style="text-align: left">제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>

					<c:set var='c' value='${pageVo.count-(pageVo.pIndex-1)*pageVo.lines }' />
					<c:forEach items='${list }' var='vo' varStatus='status'>
						<tr>
							<td>[${c-status.index }]</td>
							<td style="text-align:left; padding-left:${20*vo.depth }px">
								<c:if test="${vo.orderNo != 1}"><img id="profile" src="${pageContext.request.contextPath }/assets/images/reply.png"></c:if> 
								<c:choose>
									<c:when test="${vo.title == '[삭제된 메세지 입니다.]' && vo.contents == '[삭제]' }"><a>${vo.title }</a></c:when>
									<c:otherwise><a href="${pageContext.request.contextPath }/board/view?no=${vo.no }&pIndex=${pageVo.pIndex }&kwd=${kwd }">${vo.title }</a></c:otherwise>
								</c:choose>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
							<c:choose>
								<c:when test="${vo.title == '[삭제된 메세지 입니다.]' && vo.contents == '[삭제]' }"><a class="del"><img id="profile" src="${pageContext.request.contextPath }/assets/images/recycle.png"></a></c:when>
								<c:otherwise><a href="${pageContext.request.contextPath }/board/delete?no=${vo.no }&pIndex=${pageVo.pIndex }&kwd=${kwd }"><img id="profile" src="${pageContext.request.contextPath }/assets/images/recycle.png"></a></c:otherwise>
							</c:choose>
							</td>
						</tr>
					</c:forEach>
				</table>


				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<li><c:choose>
								<c:when test="${pageVo.prevPage >= 1 }"><a href="${pageContext.request.contextPath }/board?pIndex=${pageVo.prevPage }&kwd=${kwd }">◀</a></c:when>
								<c:otherwise>◀</c:otherwise>
							</c:choose>
						</li>
						<c:forEach var="i" begin="${pageVo.startPage }" end="${pageVo.endPage }">
							<c:choose>
								<c:when test="${pageVo.pIndex == i }"><li class="selected">${i }</li></c:when>
								<c:when test="${pageVo.ablepIndex >= i }"><li><a href="${pageContext.request.contextPath }/board?pIndex=${i }&kwd=${kwd }">${i }</a></li></c:when>
								<c:otherwise><li>${i }</li></c:otherwise>
							</c:choose>
						</c:forEach>
						<li>
						<c:choose>
							<c:when test="${pageVo.ablepIndex >= pageVo.nextPage }"><a href="${pageContext.request.contextPath }/board?pIndex=${pageVo.nextPage }&kwd=${kwd }">▶</a></c:when>
							<c:otherwise>▶</c:otherwise>
						</c:choose>
						</li>
					</ul>
				</div>
				<!-- pager 추가 -->


				<div class="bottom">
					<a href="${pageContext.request.contextPath }/board/write?pIndex=${pageVo.pIndex }&kwd=${kwd }" id="new-book">글쓰기</a>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>