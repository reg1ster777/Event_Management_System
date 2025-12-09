package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.Activity;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ActivityMapper {
    @Select("""
            select activity_id as activityId,
                   title,
                   type,
                   description,
                   start_time as startTime,
                   end_time as endTime,
                   location,
                   signup_end_time as signupEndTime,
                   max_participants as maxParticipants,
                   status,
                   live_url as liveUrl,
                   attachment_url as attachmentUrl,
                   tags,
                   created_by as createdBy,
                   created_time as createdTime
            from activity
            where activity_id = #{id}
            """)
    Activity findById(Integer id);

    @Select("""
            select activity_id as activityId,
                   title,
                   type,
                   description,
                   start_time as startTime,
                   end_time as endTime,
                   location,
                   signup_end_time as signupEndTime,
                   max_participants as maxParticipants,
                   status,
                   live_url as liveUrl,
                   attachment_url as attachmentUrl,
                   tags,
                   created_by as createdBy,
                   created_time as createdTime
            from activity
            where status <> '已删除'
            """)
    List<Activity> findAll();

    @Insert("""
            insert into activity(title, type, description, start_time, end_time, location,
                                  signup_end_time, max_participants, status, live_url, attachment_url, tags, created_by)
            values (#{title}, #{type}, #{description}, #{startTime}, #{endTime}, #{location},
                    #{signupEndTime}, #{maxParticipants}, #{status}, #{liveUrl}, #{attachmentUrl}, #{tags}, #{createdBy})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "activityId")
    int insert(Activity activity);

    @Update("""
            update activity
            set title = #{title},
                type = #{type},
                description = #{description},
                start_time = #{startTime},
                end_time = #{endTime},
                location = #{location},
                signup_end_time = #{signupEndTime},
                max_participants = #{maxParticipants},
                status = #{status},
                live_url = #{liveUrl},
                attachment_url = #{attachmentUrl},
                tags = #{tags}
            where activity_id = #{activityId}
            """)
    int update(Activity activity);

    @Update("""
            update activity
            set status = #{status}
            where activity_id = #{id}
            """)
    int updateStatusById(Integer id, String status);

    @Delete("delete from activity where activity_id = #{id}")
    int deleteById(Integer id);
}
