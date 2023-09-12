package cn.sen.lucky.infrastructure.repository;

import cn.sen.lucky.domain.activity.model.vo.StrategyVO;
import cn.sen.lucky.domain.strategy.model.aggregates.StrategyRich;
import cn.sen.lucky.domain.strategy.model.vo.AwardBriefVO;
import cn.sen.lucky.domain.strategy.model.vo.StrategyBriefVO;
import cn.sen.lucky.domain.strategy.model.vo.StrategyDetailBriefVO;
import cn.sen.lucky.domain.strategy.repository.IStrategyRepository;
import cn.sen.lucky.infrastructure.dao.IAwardDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDetailDao;
import cn.sen.lucky.infrastructure.po.Award;
import cn.sen.lucky.infrastructure.po.Strategy;
import cn.sen.lucky.infrastructure.po.StrategyDetail;
import cn.sen.lucky.infrastructure.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 13:46
 */
@Component
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyDetailDao strategyDetailDao;

    @Resource
    private IAwardDao awardDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {

        String strategyName = "Strategy_" + strategyId;
        StrategyVO strategy = (StrategyVO)redisUtil.get(strategyName);
        StrategyBriefVO strategyBriefVO = new StrategyBriefVO();
        BeanUtils.copyProperties(strategy, strategyBriefVO);
        String strategyDetailName = "StrategyDetail_" + strategyId;
//        List<?> objects = redisUtil.lGet(strategyDetailName, 0, -1);
        Object o = redisUtil.lGetIndex(strategyDetailName, 0);


//        List<StrategyDetailBriefVO> strategyDetailBriefVOList = (List<StrategyDetailBriefVO>) objects;
//        StrategyDetailBriefVO strategyDetailBriefVO = strategyDetailBriefVOList.get(0);
//        ArrayList arrayList = strategyDetailBriefVOList.get(0);


        return new StrategyRich(strategyId, strategyBriefVO, (List<StrategyDetailBriefVO>)o);
    }

    @Override
    public StrategyRich queryStrategyRich2(Long strategyId) {

        Strategy strategy = strategyDao.queryStrategy(strategyId);
        List<StrategyDetail> strategyDetailList = strategyDetailDao.queryStrategyDetailList(strategyId);

        StrategyBriefVO strategyBriefVO = new StrategyBriefVO();
        strategyBriefVO.setStrategyId(strategy.getStrategyId());
        strategyBriefVO.setStrategyDesc(strategy.getStrategyDesc());
        strategyBriefVO.setStrategyMode(strategy.getStrategyMode());
        strategyBriefVO.setGrantType(strategy.getGrantType());
        strategyBriefVO.setGrantDate(strategy.getGrantDate());
        strategyBriefVO.setExtInfo(strategy.getExtInfo());

        List<StrategyDetailBriefVO> strategyDetailBriefVOList = new ArrayList<>();
        for (StrategyDetail strategyDetail : strategyDetailList) {
            StrategyDetailBriefVO  strategyDetailBriefVO = new StrategyDetailBriefVO();
            strategyDetailBriefVO.setStrategyId(strategyDetail.getStrategyId());
            strategyDetailBriefVO.setAwardId(strategyDetail.getAwardId());
            strategyDetailBriefVO.setAwardName(strategyDetail.getAwardName());
            strategyDetailBriefVO.setAwardCount(strategyDetail.getAwardCount());
            strategyDetailBriefVO.setAwardSurplusCount(strategyDetail.getAwardSurplusCount());
            strategyDetailBriefVO.setAwardRate(strategyDetail.getAwardRate());
            strategyDetailBriefVOList.add(strategyDetailBriefVO);
        }

        return new StrategyRich(strategyId, strategyBriefVO, strategyDetailBriefVOList);

    }

    @Override
    public AwardBriefVO queryAwardInfo(String awardId) {

        Award award = awardDao.queryAwardInfo(awardId);

        // 可以使用 BeanUtils.copyProperties(award, awardBriefVO)、或者基于ASM实现的Bean-Mapping，但在效率上最好的依旧是硬编码
        AwardBriefVO awardBriefVO = new AwardBriefVO();
        awardBriefVO.setAwardId(award.getAwardId());
        awardBriefVO.setAwardType(award.getAwardType());
        awardBriefVO.setAwardName(award.getAwardName());
        awardBriefVO.setAwardContent(award.getAwardContent());

        return awardBriefVO;
    }

    @Override
    public List<String> queryNoStockStrategyAwardList(Long strategyId) {
        return strategyDetailDao.queryNoStockStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyVO> queryAllStrategy() {
        List<Strategy> strategies = strategyDao.queryAllStrategy();
        List<StrategyVO> strategyVOS = new ArrayList<>();
        for (Strategy strategy: strategies) {
            StrategyVO strategyVO = new StrategyVO();
            BeanUtils.copyProperties(strategy, strategyVO);
            strategyVOS.add(strategyVO);
        }
        return strategyVOS;
    }

    @Override
    public List<StrategyDetailBriefVO> queryStrategyDetailList(Long strategyId) {
        List<StrategyDetail> strategyDetailList = strategyDetailDao.queryStrategyDetailList(strategyId);
        List<StrategyDetailBriefVO> strategyDetailBriefVOList = new ArrayList<>();
        for (StrategyDetail strategyDetail : strategyDetailList) {
            StrategyDetailBriefVO strategyDetailBriefVO = new StrategyDetailBriefVO();
            BeanUtils.copyProperties(strategyDetail, strategyDetailBriefVO);
            strategyDetailBriefVOList.add(strategyDetailBriefVO);
        }
        return strategyDetailBriefVOList;
    }

    @Override
    public boolean deductStock(Long strategyId, String awardId) {
        StrategyDetail req = new StrategyDetail();
        req.setStrategyId(strategyId);
        req.setAwardId(awardId);
        int count = strategyDetailDao.deductStock(req);
        return count == 1;
    }

}

