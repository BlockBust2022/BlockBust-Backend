package com.springboot.streamservice.service.impl;

import com.springboot.streamservice.bean.UserBean;
import com.springboot.streamservice.dao.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CommonDao commonDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserBean user = null;
        try{
            user = commonDao.getUserfromUserName(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(null != user)
            return new User(user.getUserName(), user.getPassword(), new ArrayList<>());

        return null;
    }
}
