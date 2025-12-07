package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.Registration;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RegistrationMapper {
    @Insert("""
            insert into registration(activity_id, name, phone, school, college, clazz, student_no, email, created_time)
            values (#{activityId}, #{name}, #{phone}, #{school}, #{college}, #{clazz}, #{studentNo}, #{email}, #{createdTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "registrationId")
    int insert(Registration registration);

    @Select("select count(*) from registration where activity_id = #{activityId}")
    int countByActivityId(Integer activityId);

    @Select("select count(*) from registration where activity_id = #{activityId} and phone = #{phone}")
    int countByActivityIdAndPhone(Integer activityId, String phone);

    @Select("select count(*) from registration where activity_id = #{activityId} and student_no = #{studentNo}")
    int countByActivityIdAndStudentNo(Integer activityId, String studentNo);

    @Select("""
            select registration_id as registrationId,
                   activity_id as activityId,
                   name,
                   phone,
                   school,
                   college,
                   clazz,
                   student_no as studentNo,
                   email,
                   created_time as createdTime
            from registration
            where activity_id = #{activityId}
            order by created_time desc
            """)
    List<Registration> findByActivityId(Integer activityId);
}
