package cn.lycodeing;

import cn.lycodeing.mapper.UserMapper;
import cn.lycodeing.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringMybatisShardingJdbcApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void insertTest() {
        for (int i = 0; i < 10000; i++) {
            userMapper.insert(new User((long) i, i+1, "lycodeing_" + i, 1));
        }
    }


    @Test
    void selectTest() {
        userMapper.selectAll().forEach(System.out::println);
//        System.out.println("========");
//        userMapper.selectByPage( 0, 5).forEach(System.out::println);
//        System.out.println("=========");
//        userMapper.selectByPage( 5, 5).forEach(System.out::println);
    }
}
