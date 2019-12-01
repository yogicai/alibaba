package com.example.repository;

import com.example.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeptRepository extends JpaRepository<Dept,Integer> {

    @Query("select d from Dept d where d.dname = :dn order by deptno desc")
    public List<Dept> findDepts(@Param("dn") String dname);


}