package com.xiao.springsecuritydemo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailServiceImpl implements DbUserDetailService{

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        QueryWrapper<Users> wrapper = new QueryWrapper<>();
//        wrapper.ge("username", username);
//        Users users = usersMapper.selectOne(wrapper);
//        if (StringUtils.isEmpty(users)) {//数据库没有用户名，认证失败
//            throw new UsernameNotFoundException("用户名不存在！");
//        }
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");//模拟权限集合
        if (!"user".equals(s)){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new User("user",new BCryptPasswordEncoder().encode("password"),auths);
    }
}
