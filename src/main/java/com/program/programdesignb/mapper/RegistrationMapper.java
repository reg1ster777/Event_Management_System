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
            insert into registration(activity_id, name, phone, college)
            values (#{activityId}, #{name}, #{phone}, #{college})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "regId")
    int insert(Registration registration);

    @Select("""
            select reg_id as regId,
                   activity_id as activityId,
                   name,
                   phone,
                   college,
                   reg_time as regTime
            from registration
            where activity_id = #{activityId}
            """)
    List<Registration> findByActivityId(Integer activityId);

    @Select("""
            select reg_id as regId,
                   activity_id as activityId,
                   name,
                   phone,
                   college,
                   reg_time as regTime
            from registration
            where activity_id = #{activityId} and phone = #{phone}
            """)
    Registration findByActivityIdAndPhone(Integer activityId, String phone);
}
