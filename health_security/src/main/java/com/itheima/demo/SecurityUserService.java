package com.itheima.demo;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private HashMap map = new HashMap();

    //将map作为数据库来用，使用initData()方法来map
    public void initData(){
        User user1 = new User();
        user1.setUsername("zhangsan");
        user1.setPassword(bCryptPasswordEncoder.encode("1234"));

        User user2 = new User();
        user2.setUsername("lisi");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);

    }

    /**
     *
     * @param username :username即登陆页面用户提交的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //初始化map，将map作为数据库来使用
        initData();
        //当用户登录时，会将username传递进来，此时模拟使用username从数据库查询到user.
        User user = (User) map.get(username);

        /**
         * 开始授权：后期需要改为查询数据库动态获得用户拥有的权限和角色
         * 规则：
         *      小写的字符串是用来进行授权的
         *      大写的字符串是用来设置角色的，并且一般都有一个ROLE_前缀
         */
        List<GrantedAuthority> list = new ArrayList<>();
        /*list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("add"));*/

        //给张三添加add权限
        if (user.getUsername().equals("zhangsan")) {
            list.add(new SimpleGrantedAuthority("add"));
            //给zhangsan添加角色权限
            list.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        //声明使用明文密码
        //String password = "{noop}"+user.getPassword();

        //将用户名、密码和权限授权对象封装到security框架提供的User中
        org.springframework.security.core.userdetails.User detailUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), list);

        return  detailUser;
    }

    public static void main(String[] args) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode1 = bCryptPasswordEncoder.encode("1234");
        String encode2 = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode1);
        System.out.println(encode2);

        //使用明文密码和加密后的密码进行匹配
        System.out.println(bCryptPasswordEncoder.matches("1234",encode2));
        System.out.println(bCryptPasswordEncoder.matches("1234",encode1));

    }
}