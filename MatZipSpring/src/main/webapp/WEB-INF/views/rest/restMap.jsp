		<%@ page language="java" contentType="text/html; charset=UTF-8"
		   pageEncoding="UTF-8"%>
		<style>
		.label {margin-bottom: 96px;}
		.label * {display: inline-block;vertical-align: top;}
		.label .left {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_l.png") no-repeat;display: inline-block;height: 24px;overflow: hidden;vertical-align: top;width: 7px;}
		.label .center {background: url(https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_bg.png) repeat-x;display: inline-block;height: 24px;font-size: 12px;line-height: 24px;}
		.label .right {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_r.png") -1px 0  no-repeat;display: inline-block;height: 24px;overflow: hidden;width: 6px;}
		.center {color : #27233d;}
		
		</style>
		
		<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=6bb62151ddeddee6978f9dd897043e8e"></script>
		
		<div id="mapContainer"></div>
		
		<script type="text/javascript">
		   // 지도 표시 기능
		   var container = document.getElementById('mapContainer'); //지도를 담을 영역의 DOM 레퍼런스
		   
		   container.style.width = '100%';
		   container.style.height = '100%';
		   
		   var markerList = [] //마커리스트 생성
		   
		   const options = { //지도를 생성할 때 필요한 기본 옵션
		      center : new kakao.maps.LatLng(35.8641294, 128.5942331), //지도의 중심좌표.
		      level : 5 //지도의 레벨(확대, 축소 정도)
		   };
		   const map = new kakao.maps.Map(mapContainer, options); // 옵션을 바탕으로 새 지도 생성
		   
		  
				   
		   // 지도 표시 기능
		
		   // 지도 마커 기능
		   function getRestaurantList() {
			   
			   markerList.forEach(function(marker){
				   marker.setMap(null) //마커리스트 안에 있는 마커를 이동할 때 마다 null
			   })
			   
			   const bounds = map.getBounds()
			   const southWest = bounds.getSouthWest() 
			   const northEast = bounds.getNorthEast()
			   
			   //console.log('southWest :' + southWest)
			   //console.log('northEast :' + northEast)
			   
			   const sw_lat = southWest.getLat()
			   const sw_lng = southWest.getLng()
			   const ne_lat = northEast.getLat()
			   const ne_lng = northEast.getLng()
			   
			   //console.log('sw_lat :' + sw_lat)
			   //console.log('sw_lng :' + sw_lng)
			   //console.log('ne_lat :' + ne_lat)
			   //console.log('ne_lng :' + ne_lng)
			   
		      axios.get('/rest/ajaxGetList' , {
				   params: {
					   sw_lat, sw_lng, ne_lat, ne_lng
				   }
			   }).then(function(res) {
				//console.log(res.data)
				
		         res.data.forEach(function(item) {
		            createMarker(item)
		         })
		      })
		   }
		   
		   kakao.maps.event.addListener(map, 'tilesloaded', getRestaurantList)
		   
		   // 지도 마커 기능
		   
		   // 마커 생성 함수
		   function createMarker(item) {
		       var content = document.createElement('div');
		       content.className = 'label';
		       var leftSpan = document. createElement('span');
		       leftSpan.className = 'left';
		       var rightSpan = document. createElement('span');
		       rightSpan.className = 'right';
		       var centerSpan = document. createElement('span');
		       centerSpan.className = 'center'
		       
		       var restNm = item.nm
		       
		       if(item.is_favorite == 1) {
		    	   restNm += '♥'
		       }
		       centerSpan.innerText = restNm
		
		       content.append(leftSpan);
		       content.append(centerSpan);
		       content.append(rightSpan);
		      
		      
		      var mPos = new kakao.maps.LatLng(item.lat, item.lng);
				//console.log(mPos);
		      
		      var marker = new kakao.maps.CustomOverlay({
		          position: mPos,
		          content: content,
				//clickable: true
		      });
				//console.log(marker);
		
		      addEvent(content, 'click', function() {
				//console.log('마커 클릭: ' + item.i_rest);
		         moveToDetail(item.i_rest);
		      });
		      
		      marker.setMap(map);
		      
		      markerList.push(marker);
		   }
		   // 마커 생성 함수
		
		   // 마커 클릭 시 이벤트 함수
		   function addEvent(target, type, callback) {
		       if (target.addEventListener) {
		          target.addEventListener(type, callback);
		       } else {
		           target.attachEvent('on' + type, callback);
		       }
		   }
		   // 마커 클릭 시 이벤트 함수
		   
		   // 페이지 이동 함수
		   function moveToDetail(i_rest) {
		      location.href = '/rest/detail?i_rest=' + i_rest;
		   }
		   // 페이지 이동 함수
		   
		   // 마커 클릭 시 이벤트 함수
		   function addEvent(target, type, callback) {
		      if (target.addEventListener) {
		         target.addEventListener(type, callback);
		      } else {
		         target.addEventListener('on' + type, callback);
		      }
		   }
		   // 마커 클릭 시 이벤트 함수
		   
		   // 내 위치로 지도 이동
		   if (navigator.geolocation) {
		      //console.log('Geolocation is supported!');
		     
		      var startPos;
		      navigator.geolocation.getCurrentPosition(function(pos) {
		      startPos = pos           
				//console.log('lat : ' + startPos.coords.latitude)
				//console.log('lng : ' + startPos.coords.longitude)       
		         if(map) {
		             var moveLatLon = new kakao.maps.LatLng(startPos.coords.latitude, startPos.coords.longitude)
		             map.panTo(moveLatLon)
		         }
		       });
		     
		   } else {
		     //console.log('Geolocation is not supported for this Browser/OS.');
		   }
		   // 내 위치로 지도 이동
		</script>
			
			
