package cn.sen.lucky.rpc.res;

import cn.sen.lucky.common.Result;
import cn.sen.lucky.rpc.dto.AwardDTO;

import java.io.Serializable;

/**
 * @Author caosen
 * @Date 2023/4/4 19:38
 */
public class DrawRes extends Result implements Serializable {

    private AwardDTO awardDTO;

    public DrawRes(String code, String info) {
        super(code, info);
    }

    public AwardDTO getAwardDTO() {
        return awardDTO;
    }

    public void setAwardDTO(AwardDTO awardDTO) {
        this.awardDTO = awardDTO;
    }

}
