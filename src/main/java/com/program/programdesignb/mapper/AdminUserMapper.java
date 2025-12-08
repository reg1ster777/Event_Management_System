package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.AdminUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminUserMapper {
    @Select("""
            select user_id as userId,
                   username,
                   password,
                   role
            from admin_user
            where username = #{username}
            """)
    AdminUser findByUsername(String username);

    @Insert("""
            insert into admin_user(username, password, role)
            values (#{username}, #{password}, #{role})
            """)
    int insert(AdminUser adminUser);
}
