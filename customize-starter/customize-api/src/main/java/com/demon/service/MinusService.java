package com.demon.service;

import com.demon.exception.MinusException;

public interface MinusService {
    /**
     * 普通减法
     * @param minuend 减数
     * @param subtraction 被减数
     * @return 差
     */
    int minus(int minuend, int subtraction) throws MinusException;
}