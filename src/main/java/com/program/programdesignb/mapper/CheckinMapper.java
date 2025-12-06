package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.Checkin;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CheckinMapper {

    @Insert("""
            insert into checkin(reg_id, location)
            values (#{regId}, #{location})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "checkinId")
    int insert(Checkin checkin);

    @Select("""
            select checkin_id as checkinId,
                   reg_id as regId,
                   checkin_time as checkinTime,
                   location
            from checkin
            where reg_id = #{regId}
            """)
    List<Checkin> findByRegId(Integer regId);
}
