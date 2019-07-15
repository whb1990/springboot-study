package com.springboot.whb.study.modules.seckill.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 秒杀成功明细表
 * </p>
 *
 * @author whb123
 * @since 2019-07-15
 */
@TableName("success_killed")
public class SuccessKilled implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀商品id
     */
    private Long seckillId;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 状态标示：-1指无效，0指成功，1指已付款
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;


    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                ", seckillId=" + seckillId +
                ", userId=" + userId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", userId=" + userId +
                "}";
    }
}
