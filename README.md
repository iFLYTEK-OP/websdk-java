# 讯飞开放平台AI能力-JAVASDK

[![Build Status](https://www.travis-ci.com/iFLYTEK-OP/websdk-java.svg?branch=feature-ci)](https://www.travis-ci.com/iFLYTEK-OP/websdk-java)[![codecov](https://codecov.io/gh/iFLYTEK-OP/websdk-java/branch/feature-ci/graph/badge.svg?token=KQRe0Igv9b)](https://codecov.io/gh/iFLYTEK-OP/websdk-java)

提供各种讯飞开放平台能力的JAVASDK。

### 语音相关能力
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-speech</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```
[1、非实时语音转写文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/LFASR.md)

[2、实时语音转写文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/RTASR.md)

[3、在线语音合成文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/TTS.md)

[4、语音听写文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/IAT.md)

[5、语音评测文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/ISE.md)

[6、语音评测（普通版）](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/ISE_HTTP.md)

[7、性别年龄识别](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/IGR.md)

[8、歌曲识别](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/QBH.md)

[9、客服能力中间件](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/TELROBOT.md)

### 自然语言处理
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-nlp</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```

[1、自然语言处理文档](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/nlp/LTP.md)

[2、情感分析文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/nlp/SA.md)

[3、文本纠错文档](https://github.com/iFLYTEK-OP/websdk-java-speech/blob/master/doc/nlp/TEXT_CHECK.md)

[4、小牛翻译及自研机器翻译](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/nlp/TRANSLATE.md)


### 图片文字识别
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-ocr</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```

[1、银行卡识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/BANK_CARD.md)

[2、名片识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/BUSINESS_CARD.md)

[3、指尖文字识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/FINGER_OCR.md)

[4、印刷文字识别和手写文字识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/GENERAL_WORDS.md)

[5、图片类识别（营业执照 出租车发票 火车票 增值税发票 身份证 印刷文字）](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/IMAGE_WORD.md)

[6、INTSIG_OCR(身份证识别 营业执照识别 增值税发票识别 印刷文字识别（多语种））](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/INTSIG_OCR.md)

[7、JD_OCR（行驶证识别 驾驶证识别  车牌识别）](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/JD_OCR.md)

[8、拍照速算识别和公式识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/ITR.md)

[9、场景识别和物体识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/IMAGE_REC.md)

[10、场所识别](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/ocr/PLACE.md)

### 人脸处理能力
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-face-detector</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```

[1、静默活体检测](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/ANTI_SPOOF.md)

[2、人脸比对](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/FACE_COMPARE.md)

[3、人脸检测和属性分析](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/FACE_DETECT.md)

[4、配合式活体检测](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/FACE_STATUS.md)

[5、人脸比对sensetime](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/FACE_VER.md)

[6、活体检查sensetime](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/SILENT_DETECTION.md)

[7、人脸特征分析(性别 年龄 表情 颜值)](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/TUP_API.md)

[8、人脸水印照比对](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/face/WATER_MARK.md)


### 星火大模型相关
```xml
<dependency>
    <groupId>cn.xfyun</groupId>
    <artifactId>websdk-java-spark</artifactId>
    <!--请替换成最新稳定版本-->
    <version>2.0.3</version>
</dependency>
```

[1、讯飞智文PPT-API](https://github.com/iFLYTEK-OP/websdk-java/blob/master/doc/pptapi.md)