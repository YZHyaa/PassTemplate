package com.llxs.passbook.passbook;

import com.llxs.passbook.passbook.dao.PassTemplateDao;
import com.llxs.passbook.passbook.entity.PassTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPassTemplateDao {

    @Autowired
    private PassTemplateDao passTemplateDao;

    @Test
    public void findByLimitIsGreaterThan() {
        List<PassTemplate> list = passTemplateDao.findByLimitIsGreaterThan(0);
        System.out.println(list);
    }

    @Test
    public void findByAuditIsTrueAndLimitIsGreaterThan() {
        List<PassTemplate> list = passTemplateDao.findByPassIsTrueAndLimitGreaterThan( 0);
        System.out.println(list);
    }

}
