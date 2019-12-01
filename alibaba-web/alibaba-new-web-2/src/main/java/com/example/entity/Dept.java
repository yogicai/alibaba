package com.example.entity;

import javax.persistence.*;

@Entity //告诉SpringBoot这是一个实体类，在SB启动的时候会加载这个类
@Table(name="dept") //Dept类对应dept表
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="deptno")
    private Integer deptno;
    @Column(name="dname")
    private String dname;
    @Column(name="loc")
    private String location;

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
