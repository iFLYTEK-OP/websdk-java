# 讯飞开放平台AI能力-JAVASDK语音能力库
#### 实时语音转写
##### 示例代码
```java
//使用仅创建一个webSocket连接，需要用户自己处理分段 和 发送结束标识。否则服务端返回的结果不完善

          // step1: 创建 rtasrClient   		
          RtasrClient rtasrClient = new RtasrClient.Builder().signature("xxxxx", "xxxxxxxxx").build();

           CountDownLatch latch = new CountDownLatch(1);
   	    
           // setp2: 建立 websocket连接               
           WebSocket webSocket = rtasrClient.newWebSocket(new AbstractRtasrWebSocketListener() {
   			@Override
   			public void onSuccess(WebSocket webSocket, String text) {
                              // step4 : 接受服务端返回信息   				
                              System.out.println(text);
   			}
   
   			@Override
   			public void onFail(WebSocket webSocket, Throwable t, @Nullable Response response) {
   				latch.countDown();
   			}
   
   			@Override
   			public void onBusinessFail(WebSocket webSocket, String text) {
   				System.out.println(text);
   				latch.countDown();
   			}
   
   			@Override
   			public void onClosed() {
   				latch.countDown();
   			}
   		});
   
   		try {
   			byte[] bytes = new byte[1280];
   			File file = new File(resourcePath + filePath);
   			RandomAccessFile raf = new RandomAccessFile(file, "r");
   			int len = -1;
   			long lastTs = 0;
   			while ((len = raf.read(bytes)) != -1) {
   				if (len < 1280) {
   					bytes = Arrays.copyOfRange(bytes, 0, len);
   					webSocket.send(ByteString.of(bytes));
   					break;
   				}
   
   				long curTs = System.currentTimeMillis();
   				if (lastTs == 0) {
   					lastTs = System.currentTimeMillis();
   				} else {
   					long s = curTs - lastTs;
   					if (s < 40) {
   						System.out.println("error time interval: " + s + " ms");
   					}
   				}

                               // setp3: 发送数据        
   				webSocket.send(ByteString.of(bytes));

   				// 每隔40毫秒发送一次数据
   				Thread.sleep(40);
   			}

   			// step5: 发送结束标识
   			rtasrClient.sendEnd();
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   		latch.await();
```
更详细请参见[Demo](https://github.com/iFLYTEK-OP/websdk-java-demo/blob/main/src/main/java/cn/xfyun/demo/RtasrClientApp.java)
##### 实时语音转写参数
|参数名|类型|必传|描述|示例|
|---|---|---|---|---|
|punc|string|否|标点过滤控制，默认返回标点。<br>0:返回标点（默认值） <br>1:不返回标点|punc="0"|
|pd|string|否|垂直领域个性化参数:<br>法院: court <br>教育: edu <br>金融:finance <br> 医疗:medical<br> 科技: tech|设置示例：pd="edu" <br>参数pd为非必须设置，不设置参数默认为通用|

*注：详细的参数可以参见[业务参数](https://www.xfyun.cn/doc/asr/rtasr/API.html)
