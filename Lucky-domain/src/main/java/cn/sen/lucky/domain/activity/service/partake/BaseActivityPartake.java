package cn.sen.lucky.domain.activity.service.partake;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.activity.model.req.PartakeReq;
import cn.sen.lucky.domain.activity.model.res.PartakeResult;
import cn.sen.lucky.domain.activity.model.res.StockResult;
import cn.sen.lucky.domain.activity.model.vo.ActivityBillVO;
import cn.sen.lucky.domain.activity.model.vo.ActivityVO;
import cn.sen.lucky.domain.activity.model.vo.UserTakeActivityVO;
import cn.sen.lucky.domain.support.ids.IIdGenerator;
import cn.sen.lucky.domain.util.RedisUti2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 活动领取模板抽象类
 * @Author caosen
 * @Date 2023/4/3 10:23
 */
public abstract class BaseActivityPartake extends ActivityPartakeSupport implements IActivityPartake {

    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    private Logger logger = LoggerFactory.getLogger(BaseActivityPartake.class);

    @Resource
    private RedisUti2 redisUtil2;
    @Override
    public PartakeResult doPartake(PartakeReq req) {

// 1. 查询是否存在未执行抽奖领取活动单【user_take_activity 存在 state = 0，领取了但抽奖过程失败的，可以直接返回领取结果继续抽奖】
//        UserTakeActivityVO userTakeActivityVO = this.queryNoConsumedTakeActivityOrder(req.getActivityId(), req.getuId());
//        if (null != userTakeActivityVO) {
//            return buildPartakeResult(userTakeActivityVO.getStrategyId(), userTakeActivityVO.getTakeId(), Constants.ResponseCode.NOT_CONSUMED_TAKE);
//        }

        // 2. 查询活动账单，耗时较长
        ActivityBillVO activityBillVO = super.queryActivityBill(req);
//        logger.info("{}",activityBillVO.getStockSurplusCount());
        // 3. 活动信息校验处理【活动库存、状态、日期、个人参与次数】
        Result checkResult = this.checkActivityBill(req, activityBillVO);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(checkResult.getCode())) {
            return new PartakeResult(checkResult.getCode(), checkResult.getInfo());
        }

        // 4. 扣减活动库存，通过Redis【活动库存扣减编号，作为锁的Key，缩小颗粒度】 Begin（也就是在这里加锁，锁的问题就出现在这里）
        //这里加了分布式锁，
        StockResult subtractionActivityResult = this.subtractionActivityStockByRedis(req.getuId(), req.getActivityId(), activityBillVO.getStockCount());

        if (!Constants.ResponseCode.SUCCESS.getCode().equals(subtractionActivityResult.getCode())) {
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), subtractionActivityResult.getCode());
            return new PartakeResult(subtractionActivityResult.getCode(), subtractionActivityResult.getInfo());
        }
        activityBillVO.setStockSurplusCount(subtractionActivityResult.getStockSurplusCount());
        // 5. 插入领取活动信息【个人用户把活动信息写入到用户表】
        Long takeId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        Result grabResult = this.grabActivity(req, activityBillVO, takeId);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(grabResult.getCode())) {
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), grabResult.getCode());
            return new PartakeResult(grabResult.getCode(), grabResult.getInfo());
        }

        // 6. 扣减活动库存，通过Redis End
        this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), Constants.ResponseCode.SUCCESS.getCode());

        return buildPartakeResult(activityBillVO.getStrategyId(), takeId, activityBillVO.getStockCount(), subtractionActivityResult.getStockSurplusCount(), Constants.ResponseCode.SUCCESS);

    }

    /**
     * 用于压测
     * @param req
     * @return
     */
    @Override
    public PartakeResult doPartake2(PartakeReq req) {

// 1. 查询是否存在未执行抽奖领取活动单【user_take_activity 存在 state = 0，领取了但抽奖过程失败的，可以直接返回领取结果继续抽奖】
//        UserTakeActivityVO userTakeActivityVO = this.queryNoConsumedTakeActivityOrder(req.getActivityId(), req.getuId());
//        if (null != userTakeActivityVO) {
//            return buildPartakeResult(userTakeActivityVO.getStrategyId(), userTakeActivityVO.getTakeId(), Constants.ResponseCode.NOT_CONSUMED_TAKE);
//        }

        // 2. 查询活动账单，耗时较长
        ActivityBillVO activityBillVO = super.queryActivityBill2(req);
//        logger.info("{}",activityBillVO.getStockSurplusCount());
        // 3. 活动信息校验处理【活动库存、状态、日期、个人参与次数】
        Result checkResult = this.checkActivityBill(req, activityBillVO);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(checkResult.getCode())) {
            return new PartakeResult(checkResult.getCode(), checkResult.getInfo());
        }

        // 4. 扣减活动库存，通过Redis【活动库存扣减编号，作为锁的Key，缩小颗粒度】 Begin（也就是在这里加锁，锁的问题就出现在这里）
        //这里加了分布式锁，
        StockResult subtractionActivityResult = this.subtractionActivityStockByRedis(req.getuId(), req.getActivityId(), activityBillVO.getStockCount());

        if (!Constants.ResponseCode.SUCCESS.getCode().equals(subtractionActivityResult.getCode())) {
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), subtractionActivityResult.getCode());
            return new PartakeResult(subtractionActivityResult.getCode(), subtractionActivityResult.getInfo());
        }
        activityBillVO.setStockSurplusCount(subtractionActivityResult.getStockSurplusCount());
        // 5. 插入领取活动信息【个人用户把活动信息写入到用户表】
        Long takeId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        Result grabResult = this.grabActivity2(req, activityBillVO, takeId);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(grabResult.getCode())) {
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), grabResult.getCode());
            return new PartakeResult(grabResult.getCode(), grabResult.getInfo());
        }

        // 6. 扣减活动库存，通过Redis End
        this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), Constants.ResponseCode.SUCCESS.getCode());

        return buildPartakeResult(activityBillVO.getStrategyId(), takeId, activityBillVO.getStockCount(), subtractionActivityResult.getStockSurplusCount(), Constants.ResponseCode.SUCCESS);

    }

    /**
     * 恢复活动库存，通过Redis 【如果非常异常，则需要进行缓存库存恢复，只保证不超卖的特性，所以不保证一定能恢复占用库存，另外最终可以由任务进行补偿库存】
     *
     * @param activityId 活动ID
     * @param tokenKey   分布式 KEY 用于清理
     * @param code       状态
     */
    protected abstract void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code);

    private PartakeResult buildPartakeResult(Long strategyId, Long takeId) {
        PartakeResult partakeResult = new PartakeResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        partakeResult.setStrategyId(strategyId);
        partakeResult.setTakeId(takeId);
        return partakeResult;
    }

    private PartakeResult buildPartakeResult(Long strategyId, Long takeId, Integer stockCount, Integer stockSurplusCount, Constants.ResponseCode code) {
        PartakeResult partakeResult = new PartakeResult(code.getCode(), code.getInfo());
        partakeResult.setStrategyId(strategyId);
        partakeResult.setTakeId(takeId);
        partakeResult.setStockCount(stockCount);
        partakeResult.setStockSurplusCount(stockSurplusCount);
        return partakeResult;
    }

    /**
     * 封装结果【返回的策略ID，用于继续完成抽奖步骤】
     *
     * @param strategyId 策略ID
     * @param takeId     领取ID
     * @param code       状态码
     * @return 封装结果
     */
    private PartakeResult buildPartakeResult(Long strategyId, Long takeId, Constants.ResponseCode code) {
        PartakeResult partakeResult = new PartakeResult(code.getCode(), code.getInfo());
        partakeResult.setStrategyId(strategyId);
        partakeResult.setTakeId(takeId);
        return partakeResult;
    }
    /**
     * 扣减活动库存，通过Redis
     *
     * @param uId        用户ID
     * @param activityId 活动号
     * @param stockCount 总库存
     * @return 扣减结果
     */
    protected abstract StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount);

    /**
     * 查询是否存在未执行抽奖领取活动单【user_take_activity 存在 state = 0，领取了但抽奖过程失败的，可以直接返回领取结果继续抽奖】
     *
     * @param activityId 活动ID
     * @param uId        用户ID
     * @return 领取单
     */
    protected abstract UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId);


    /**
     * 活动信息校验处理，把活动库存、状态、日期、个人参与次数
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 校验结果
     */
    protected abstract Result checkActivityBill(PartakeReq partake, ActivityBillVO bill);

    /**
     * 扣减活动库存
     *
     * @param req 参与活动请求
     * @return 扣减结果
     */
    protected abstract Result subtractionActivityStock(PartakeReq req);

    /**
     * 领取活动
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 领取结果
     */
    protected abstract Result grabActivity(PartakeReq partake, ActivityBillVO bill, Long takeId);

    protected abstract Result grabActivity2(PartakeReq partake, ActivityBillVO bill, Long takeId);

}

