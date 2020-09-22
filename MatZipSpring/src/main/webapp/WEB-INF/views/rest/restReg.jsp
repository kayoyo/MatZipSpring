<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="sectionContainerCenter">
<div>
	<form id="frm" action="/rest/restReg" method="post" onsubmit="return chkFrm()">
		<div><input type="text" name="nm" placeholder="상호명" style="width:200px;"></div>
		<div><input type="text" name="addr" placeholder="주소" onkeyup="changeAddr()" style="width:200px;">
				<button type="button" onclick="getLatLng()">location</button><span id="resultGetLatLng"></span>
		</div>
			<input type="hidden" name="i_user" value="${loginUser.i_user}">
			<input type="hidden" name="lat" value="0">
			<input type="hidden" name="lng" value="0">
		<div>
			 <select name="cd_category">
			 	<option value="0">category</option>
			 	<c:forEach items="${categoryList}" var="item">
			 		<option value="${item.cd}"> ${item.val}</option> <!--예)1번 분식-->
			 	</c:forEach>
			 </select>
		</div>
		<div><input type="submit" value="등록"></div>
	</form>
	</div>
	
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ca5f62046c5a8e09b97cff7ef9b0f4d8&libraries=services"></script>
   	<script type="text/javascript">
   	  function chkFrm() {
   		  if(frm.nm.value.length == 0){
   			  alert('가게명을 입력 해 주세요')
   			  frm.nm.focus()
   		  	  return false
   		  } else if(frm.addr.value.length < 9){
   			 alert('주소를 확인 해 주세요')
  			 frm.addr.focus()
  			 return false
   		  } else if(frm.lat.value == '0' || frm.lng.value == '0'){
   			alert('좌표값을 가져와 주세요')
   			return false 
   		  } else if(frm.cd_category.value == '0'){
   			alert('카테고리를 선택 해 주세요')
   			frm.cd_category.focus()
 			return false
   		  }
   	  }
   	
   	  function changeAddr() {
   		  resultGetLatLng.innerText = ""
   		  frm.lat.value = '0'
   		  frm.lng.value = '0'
   	  }
      // 검색된 주소 좌표 찍기 구현
      const geocoder = new kakao.maps.services.Geocoder()
      function getLatLng() {
         event.preventDefault()
         const addrStr = frm.addr.value
         
         if(addrStr.length < 9) {
            alert('주소를 확인해주세요')
            frm.addr.focus()
            return
         }
         
         geocoder.addressSearch(addrStr, function(result, status) {
            if(status === kakao.maps.services.Status.OK) {
               console.log(result[0])
               
               if(result.length > 0) {
			        	resultGetLatLng.innerText = 'V'
			        	frm.lat.value = result[0].y
				        frm.lng.value = result[0].x
               }
            }
         });
      }
      // 검색된 주소 좌표 찍기 구현
   </script>
</div>