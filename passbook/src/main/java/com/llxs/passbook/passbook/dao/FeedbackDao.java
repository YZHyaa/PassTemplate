package com.llxs.passbook.passbook.dao;

import com.llxs.passbook.passbook.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FeedbackDao extends JpaRepository<Feedback,Long> {

    /**
     * 查询某一用户 ID
     */
    List<Feedback> findByUserId(long userId);

}
