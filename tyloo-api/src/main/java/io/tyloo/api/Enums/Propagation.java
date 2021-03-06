package io.tyloo.api.Enums;

/*
 *
 * 传播级别
 *
 * @Author:Zh1Cheung 945503088@qq.com
 * @Date: 11:49 2019/12/4
 *
 */

public enum Propagation {
    /**
     * 支持当前事务，如果当前没有事务，就新建一个事务
     */
    REQUIRED(0),
     /**
     * 支持当前事务，如果当前没有事务，就以非事务方式执行
     */
    SUPPORTS(1),
    /**
     * 支持当前事务，如果当前没有事务，就抛出异常
     */
    MANDATORY(2),
    /**
     * 新建事务，如果当前存在事务，把当前事务挂起
     */
    REQUIRES_NEW(3);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}