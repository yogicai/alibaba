package com.demon.minus;

import com.demon.exception.MinusException;
import com.demon.service.MinusService;

public class MinusServiceSupportNegativeImpl implements MinusService {

    /**
     * 减法实现，支持负数
     *
     * @param minuend     减数
     * @param subtraction 被减数
     * @return
     * @throws MinusException
     */
    @Override
    public int minus(int minuend, int subtraction) throws MinusException {
        return minuend - subtraction;
    }
}