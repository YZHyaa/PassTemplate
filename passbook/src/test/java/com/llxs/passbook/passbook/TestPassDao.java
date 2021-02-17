package com.llxs.passbook.passbook;

import com.llxs.passbook.passbook.dao.PassDao;
import com.llxs.passbook.passbook.entity.Pass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPassDao {

    @Autowired
    private PassDao passDao;

    @Test
    public void findByUserIdAndConDate() {
        List<Pass> list = passDao.findByUserIdAndConDate(1, null);
        System.out.println(list);
    }
}
