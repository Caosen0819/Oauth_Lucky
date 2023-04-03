package cn.sen.lucky.domain.support.ids;

/**
 * @Author caosen
 * @Date 2023/3/31 13:03
 * 生成id的接口
 */
public interface IIdGenerator {

    /*目前有三种算法*/

    /**
     * 雪花
     * 随机
     * 日期拼接
     * @return
     */
    Long nextId();
}
