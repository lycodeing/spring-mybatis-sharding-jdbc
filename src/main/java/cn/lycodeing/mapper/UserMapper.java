package cn.lycodeing.mapper;

import cn.lycodeing.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    int insert(User user);

    User selectById(Long id);

    List<User> selectAll();

    /**
     * 分页查询
     */
    @Select("SELECT * FROM t_user order by id desc limit #{offset}, #{limit}  ")
    List<User> selectByPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
