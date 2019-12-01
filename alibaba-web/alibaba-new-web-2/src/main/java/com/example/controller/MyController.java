package com.example.controller;

import com.example.entity.Dept;
import com.example.entity.Emp;
import com.example.mapper.EmpMapper;
import com.example.repository.DeptRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author: yogiCai
 * @create: 2019-11-18 22:01
 **/

@Controller
public class MyController {

    @Resource
    private DeptRepository deptRepository;
    @Resource
    private EmpMapper empMapper;


    @RequestMapping("/")
    @ResponseBody
    public Object index() {
        return new Date();
    }


    @RequestMapping("/out")
    @ResponseBody
    public Object out(Date sDate) {
        return sDate;
    }


    @GetMapping("/dept/{id}")
    @ResponseBody
    public Dept findById(@PathVariable("id") Integer id){
        Optional<Dept> op =  deptRepository.findById(id);
        Dept dept = null;
        if(op.isPresent() == true){
            dept = op.get();
        }
        return dept;
    }


    @RequestMapping("/emp/{id}")
    @ResponseBody
    public Emp findBy(@PathVariable("id") Integer id){
        Emp emp = empMapper.findById(id);
        return emp;
    }
}
