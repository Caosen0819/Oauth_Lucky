package cn.sen.lucky.process.draw.res;


import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.strategy.model.vo.DrawAwardVO;

public class DrawProcessResult extends Result {

    private DrawAwardVO drawAwardVO;
    public String log = "1";

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardVO drawAwardVO) {
        super(code, info);
        this.drawAwardVO = drawAwardVO;
    }

    public DrawAwardVO getDrawAwardVO() {
        return drawAwardVO;
    }

    public void setDrawAwardVO(DrawAwardVO drawAwardVO) {
        this.drawAwardVO = drawAwardVO;
    }

}
