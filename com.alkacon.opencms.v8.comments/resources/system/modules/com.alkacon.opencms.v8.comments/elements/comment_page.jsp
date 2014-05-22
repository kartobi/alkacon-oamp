<%@ page import="com.alkacon.opencms.v8.comments.*, java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	Map<String, String> dynamicConfig = CmsCommentsAccess.generateDynamicConfig(request.getParameter("cmtformid"));
	CmsCommentsAccess alkaconCmt = new CmsCommentsAccess(pageContext, request, response, request.getParameter("configUri"), dynamicConfig);
	pageContext.setAttribute("alkaconCmt", alkaconCmt);
%>
<c:if test="${alkaconCmt.config.allowReplies}">
	<jsp:useBean id="alkaconReplies" class="com.alkacon.opencms.v8.comments.CmsRepliesAccessBean" />
</c:if>
<fmt:setLocale value="${cms:vfs(pageContext).requestContext.locale}" />
<cms:bundle basename="${alkaconCmt.resourceBundle}" >
<!-- start: page -->
<c:choose>
<c:when test="${alkaconCmt.userCanManage}" >
	<c:forEach var="comment" items="${alkaconCmt.comments}" >
	   <!-- start: manager comment -->
	   <%@include file="%(link.strong:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_manager.jsp:564331dc-15df-11e1-aeb4-9b778fa0dc42)" %>
	   <!-- end: manager comment -->
	</c:forEach>
</c:when>
<c:otherwise>
	<c:forEach var="comment" items="${alkaconCmt.comments}" >
	   <!-- start: comment -->
	   <%@include file="%(link.strong:/system/modules/com.alkacon.opencms.v8.comments/elements/comment_view.jsp:564f18cb-15df-11e1-aeb4-9b778fa0dc42)" %>
	   <!-- end: comment -->
	</c:forEach>
</c:otherwise>
</c:choose>
<!-- end: page -->
</cms:bundle>
