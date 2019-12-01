package com.demon.add;

import com.demon.service.AddService;

public class AddServiceImpl implements AddService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}