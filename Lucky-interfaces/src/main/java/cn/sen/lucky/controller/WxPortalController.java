package cn.sen.lucky.controller;




import cn.sen.lucky.application.IWxReceiveService;
import cn.sen.lucky.application.IWxValidateService;
import cn.sen.lucky.common.api.CommonResult;
import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.model.MessageTextEntity;
import cn.sen.lucky.infrastructure.utils.XmlUtil;
import cn.sen.lucky.token.Oauth2TokenDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * @description: 微信公众号入口
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@RestController
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {

    private Logger logger = LoggerFactory.getLogger(WxPortalController.class);
    @Value("${wx.config.originalid:gh_89e9e6e0eb59}")
    private String originalId;

    @Resource
    private IWxValidateService wxValidateService;
    @Resource
    private IWxReceiveService wxReceiveService;

    /**
     * 处理微信服务器发来的get请求，进行签名的验证
     * <p>
     * appid     微信端AppID
     * signature 微信端发来的签名
     * timestamp 微信端发来的时间戳
     * nonce     微信端发来的随机字符串
     * echostr   微信端发来的验证字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {

            logger.info("cscscs微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            boolean check = wxValidateService.checkSign(signature, timestamp, nonce);
            logger.info("微信公cscs众号验签信息{}完成 check：{}", appid, check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            logger.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    /**
     * 此处是处理微信服务器的消息转发的
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature, HttpServletResponse response) {
        try {
            logger.info("接收aaa微信公众号信息请求{}开始 {}", openid, requestBody);

            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);
            String content = message.getContent();
            BehaviorMatter behaviorMatter = new BehaviorMatter();
            behaviorMatter.setOpenId(openid);
            behaviorMatter.setFromUserName(message.getFromUserName());
            behaviorMatter.setMsgType(message.getMsgType());
            behaviorMatter.setContent(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim());
            behaviorMatter.setEvent(message.getEvent());
            behaviorMatter.setCreateTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L));
            if (content.equals("授权")) {
                // 构建重定向URL
//                String redirectUrl = "http://8.130.78.210:8082/oauth/token?username=admin&password=macro123&client_id=myjszl&client_secret=123&grant_type=password";
                String redirectUrl = "http://8.130.78.210:8082/oauth/token?username=admin&password=macro123&client_id=myjszl&client_secret=123&grant_type=password";

                // 发送POST请求到重定向URL
                String authResponseBody = sendPostRequest(redirectUrl);
                // 处理重定向后的响应内容
                MessageTextEntity res = new MessageTextEntity();
                res.setToUserName(behaviorMatter.getOpenId());
                res.setFromUserName(originalId);
                res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
                res.setMsgType("text");
                res.setContent(authResponseBody);
                return XmlUtil.beanToXml(res);


//                response.sendRedirect("http://localhost:8082/oauth/token?username=admin&password=macro123&client_id=myjszl&client_secret=123&grant_type=password");
            } else if (content.equals("抽奖")) {

                // 处理消息
                String result = wxReceiveService.doReceive(behaviorMatter);
                logger.info("接dddd收微信公众号信息请求{}完成 {}", openid, result);
                return result;
            } else if (content.length() > 0 && content.substring(0, 6).equals("Bearer")) {
//                Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX2lkIjoiMyIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTY5MzkwNjQ1NSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ST09UIl0sImp0aSI6IjczZDljZDNhLTJmNGMtNDcwNS1iYmM2LTIzZWEyMDMwYWJmZSIsImNsaWVudF9pZCI6Im15anN6bCJ9.jXIAu1H0vRIUWlMWE6ngXhK3SUyFEf_wgTYsZIFLa8Q
                String url = "http://8.130.78.210:80/lucky-interfaces-provider/do/hello2";
                String anthorization = content.substring(7);
                String result = sendPostRequest2(url, anthorization);
                // 处理消息

                MessageTextEntity res = new MessageTextEntity();
                res.setToUserName(behaviorMatter.getOpenId());
                res.setFromUserName(originalId);
                res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
                res.setMsgType("text");
                res.setContent(result);
                return XmlUtil.beanToXml(res);

            } else {
                MessageTextEntity res = new MessageTextEntity();
                res.setToUserName(behaviorMatter.getOpenId());
                res.setFromUserName(originalId);
                res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
                res.setMsgType("text");
                res.setContent("null");
                return XmlUtil.beanToXml(res);

            }

        } catch (Exception e) {

            logger.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }
    private String sendPostRequest(String url) throws Exception {
//        logger.info("1");

        // 创建URL对象
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
//        logger.info("lianjie");
        // 设置POST请求方法
        connection.setRequestMethod("POST");

        // 启用输入流
        connection.setDoInput(true);

        // 启用输出流
        connection.setDoOutput(true);

        // 构建POST请求参数
        String postData = ""; // 根据需要构建POST请求的参数
        byte[] postDataBytes = postData.getBytes("UTF-8");

        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

        // 将POST请求参数写入输出流
        try (OutputStream os = connection.getOutputStream()) {
            os.write(postDataBytes);
        }

        // 获取响应码
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应内容
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseContent = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
//                logger.info("sdfd{}",responseContent);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(String.valueOf(responseContent));

                // 获取 data 字段中的 token 属性值
                String token = jsonNode.get("data").get("token").asText();

                // 现在，变量 token 包含了 token 的值
//                logger.info("token {}",token);
                return token;
            }
        } else {
            throw new Exception("POST请求失败，响应码：" + responseCode);
        }
    }

    private String sendPostRequest2(String url, String anthorization) throws Exception {
        // 创建URL对象
        URL postUrl = new URL(url);

        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

        // 设置POST请求方法
        connection.setRequestMethod("POST");

        // 启用输入流
        connection.setDoInput(true);

        // 启用输出流
        connection.setDoOutput(true);

        // 构建POST请求参数
        String postData = ""; // 根据需要构建POST请求的参数
        byte[] postDataBytes = postData.getBytes("UTF-8");

        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.setRequestProperty("Authorization", "Bearer " + anthorization);
        // 将POST请求参数写入输出流
        try (OutputStream os = connection.getOutputStream()) {
            os.write(postDataBytes);
        }

        // 获取响应码
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应内容
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseContent = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                String result = responseContent.toString();
                return result;
            }
        } else {
            throw new Exception("GET请求失败，响应码：" + responseCode);
        }
    }

}
