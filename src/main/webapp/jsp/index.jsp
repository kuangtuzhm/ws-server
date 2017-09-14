<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Web Socket JavaScript Echo Client</title>
  <script src="http://cdn.jsdelivr.net/sockjs/1/sockjs.min.js"></script>
  <script src="/js/jquery-1.8.3.min.js"></script>
  <script language="javascript" type="text/javascript">
  
  //var wsBaseUri = "ws://localhost/websocket";
  //var serverBaseUri = "http://localhost";
  
  var wsBaseUri = "ws://172.16.0.104/websocket";
  var serverBaseUri = "http://172.16.0.104";
  
  var heartCheck = {
		    timeout: 60000,//60s
		    timeoutObj: null,
		    serverTimeoutObj: null,
		    reset: function(){
		        clearTimeout(this.timeoutObj);
		        clearTimeout(this.serverTimeoutObj);
		        this.start();
		    },
		    start: function(){
		        var self = this;
		        this.timeoutObj = setTimeout(function(){
		        	doSend("HeartBeat");
		            self.serverTimeoutObj = setTimeout(function(){
		            	echo_websocket.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
		            }, self.timeout);
		        }, this.timeout);
		    },
	};

    var echo_websocket;
    var output;
    
    function init() {
      output = document.getElementById("output");
      var wsUri = wsBaseUri + "?jspCode=zhm";
      writeToScreen("Connecting to " + wsUri);
  
      if ('WebSocket' in window) {  
    	  console.log("11111111111");  
    	  echo_websocket = new WebSocket(wsUri);  
      } else if ('MozWebSocket' in window) {  
    	  console.log("222222222222");  
    	  echo_websocket = new MozWebSocket(wsUri);  
      } else {  
    	  wsUri = "http://localhost/sockjs/websocket?jspCode=zhm";
    	  console.log("33333333333");  
    	  echo_websocket = new SockJS(wsUri);  
      } 
      
      
      echo_websocket.onopen = function (evt) {
        writeToScreen("Connected !");
        doSend($("#textID").val());
        heartCheck.start();
      };
      echo_websocket.onmessage = function (evt) {
        writeToScreen("Received message: " + evt.data);
        heartCheck.reset();
        //echo_websocket.close();
      };
      echo_websocket.onerror = function (evt) {
    	// 0        CONNECTING        连接尚未建立
		   // 1        OPEN            WebSocket的链接已经建立
		   // 2        CLOSING            连接正在关闭
		   // 3        CLOSED            连接已经关闭或不可用
		if(evt.currentTarget.readyState == 3)
		{
			 writeToScreen('<span style="color: red;">ERROR:Connection Closed</span>');
			 heartCheck.reset();
		}
		else
		{
			writeToScreen('<span style="color: red;">ERROR:信息传输出错</span>');
		}
      };
      echo_websocket.onclose = function (evt) {
    	  writeToScreen('<span style="color: red;">CLOSE:</span> '
    	          + evt.data);
    	  echo_websocket = null;
    	  init();
      };
    }

    function send_echo() {
    	doSend($("#textID").val());
    }
    
    function serverEvent()
    {
    	//console.log(serverBaseUri+'/tuqiang-ws/ws/testWebSocket?jspCode=zhm');  
    	//$.ajax({url: (serverBaseUri+'/tuqiang-ws/ws/testWebSocket?jspCode=zhm'),async:false});
    	$.ajax({
    	    url: serverBaseUri+'/ws/testWebSocket',
    	    type:'POST', //GET
    	    async:true,    //或false,是否异步
    	    data:{
    	    	jspCode:'zhm'
    	    },
    	    timeout:5000,    //超时时间
    	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
    	    beforeSend:function(xhr){
    	        console.log(xhr)
    	        console.log('发送前')
    	    },
    	    success:function(data,textStatus,jqXHR){
    	        console.log(data)
    	        console.log(textStatus)
    	        console.log(jqXHR)
    	    },
    	    error:function(xhr,textStatus){
    	        console.log('错误')
    	        console.log(xhr)
    	        console.log(textStatus)
    	    },
    	    complete:function(){
    	        console.log('结束')
    	    }
    	});
    }
    
    function closeEvent()
    {
    	echo_websocket.close();
    }
    
    function msgEvent()
    {
    	//$.ajax({url: (serverBaseUri+'/tuqiang-ws/ws/sendMsg?jspCode=msgtest'),async:false});
    	$.ajax({
    	    url: serverBaseUri+'/ws/sendMsg',
    	    type:'POST', //GET
    	    async:true,    //或false,是否异步
    	    data:{
    	    	jspCode:'msgtest'
    	    },
    	    timeout:5000,    //超时时间
    	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
    	    beforeSend:function(xhr){
    	        console.log(xhr)
    	        console.log('发送前')
    	    },
    	    success:function(data,textStatus,jqXHR){
    	        console.log(data)
    	        console.log(textStatus)
    	        console.log(jqXHR)
    	    },
    	    error:function(xhr,textStatus){
    	        console.log('错误')
    	        console.log(xhr)
    	        console.log(textStatus)
    	    },
    	    complete:function(){
    	        console.log('结束')
    	    }
    	})
    }
    
    function doSend(message) {
      echo_websocket.send(message);
      writeToScreen("Sent message: " + message);
    }
    function writeToScreen(message) {
      var pre = document.createElement("p");
      pre.style.wordWrap = "break-word";
      pre.innerHTML = message;
      output.appendChild(pre);
    }
    //window.addEventListener("load", init, false);
    function connect()
    {
    	init();
    }
    
  </script>
</head>
<body>
<h1>Echo Server</h1>
<div style="text-align: left;">
  <form action="">
  	<input onclick="connect()" value="connect" type="button">
    <input onclick="send_echo()" value="clientEvent" type="button">
    
    <input onclick="serverEvent()" value="serverEvent" type="button">
    <input onclick="closeEvent()" value="closeEvent" type="button">
    <input onclick="msgEvent()" value="tuqiangMsgEvent" type="button">
    <input id="textID" name="message" value="Hello World, Web Sockets" type="text">
    <br>
  </form>
</div>
<div id="output"></div>
</body>
</html>
