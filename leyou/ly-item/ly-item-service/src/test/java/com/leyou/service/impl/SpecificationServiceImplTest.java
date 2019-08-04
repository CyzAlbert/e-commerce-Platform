package com.leyou.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpecificationServiceImplTest {
    @Autowired
    private SpecificationServiceImpl specificationService;

    @Test
    public void test(){
        specificationService.addSpecificationGruop(Long.valueOf(76),"newName");
    }
}
