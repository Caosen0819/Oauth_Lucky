package cn.sen.lucky.domain.validate;

import cn.sen.lucky.application.IWxValidateService;
import cn.sen.lucky.infrastructure.utils.sdk.SignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @description: 消息验证
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service
public class WxValidateServiceImpl implements IWxValidateService {

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }

}
