package com.example.scprovider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class IndexController {

    /**
     * 返回一个实体
     */
    @GetMapping("{msg}")
    public String sayHelloWorld(@PathVariable("msg") String msg) {
        System.out.println("come on " + msg);
        return "sc-provider receive : " + msg;
    }

    /**
     * 返回一个列表
     */
    @GetMapping("list")
    public List list() {
        List<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(22);
        list.add(75);
        list.add(93);
        return list;
    }
}