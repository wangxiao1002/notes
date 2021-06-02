package com.xiao.springsecuritydemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    @PreAuthorize("hasAuthority('ss')")
    @PostFilter("filterObject.id=1")
    public ResponseEntity<String> getTest () {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String aa= bCryptPasswordEncoder.encode("wangxiao");
        System.out.println(aa);
        boolean a = bCryptPasswordEncoder.matches("wangxiao",aa);
        System.out.println(a);
        return ResponseEntity.ok("helloController");
    }

}
