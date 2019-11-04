package com.itheima.dao;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberDao {
    Member findByTelephone(@Param("phoneNumber") String telephone);

    void add(Member member);

    /**
     * 根据月份统计会员数量
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);

    Integer findMemberCountByDate(String today);
}
