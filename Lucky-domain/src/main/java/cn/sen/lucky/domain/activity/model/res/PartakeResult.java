package cn.sen.lucky.domain.activity.model.res;

import cn.sen.lucky.common.Result;

/**
 * @Author caosen
 * @Date 2023/4/3 10:26
 */
public class PartakeResult extends Result {

    /**
     * 策略ID
     */
    private Long strategyId;

    public PartakeResult(String code, String info) {
        super(code, info);
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }
}