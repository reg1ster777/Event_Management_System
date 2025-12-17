package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.Checkin;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CheckinMapper {

    @Insert("""
            insert into checkin(activity_id, student_no, name, phone, method)
            values (#{activityId}, #{studentNo}, #{name}, #{phone}, #{location})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "checkinId")
    int insert(Checkin checkin);

    @Select("""
            select checkin_id as checkinId,
                   activity_id as activityId,
                   student_no as studentNo,
                   name,
                   phone,
                   checkin_time as checkinTime,
                   method as location
            from checkin
            where activity_id = #{activityId}
              and student_no = #{studentNo}
            """)
    List<Checkin> findByActivityAndStudent(@Param("activityId") Integer activityId,
                                           @Param("studentNo") String studentNo);

    @Select("""
            select checkin_id as checkinId,
                   activity_id as activityId,
                   student_no as studentNo,
                   name,
                   phone,
                   checkin_time as checkinTime,
                   method as location
            from checkin
            where activity_id = #{activityId}
            """)
    List<Checkin> findByActivityId(Integer activityId);
}
