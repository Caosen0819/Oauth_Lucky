package cn.sen.lucky.rpc.activity.booth.res;


import cn.sen.lucky.common.Result;
import cn.sen.lucky.rpc.activity.booth.dto.AwardDTO;

import java.io.Serializable;


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
