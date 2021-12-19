package com.springboot.streamservice.controller;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class CommonController {

    @Autowired
    CommonService commonService;

    @GetMapping(value = "/featured", produces = "application/json")
    public String featured() {
        return commonService.featured();
    }

    //TODO: Update Multiple entries
    @PostMapping(value = "/updateFeatured", produces = "application/json")
    public void updateFeatured(@RequestBody Featured featured) {
        commonService.updateFeatured(featured);
    }

//    @GetMapping(value = "/moveToDb", produces = "application/json")
////    @Scheduled(cron = "0 * * ? * *")
//    public String moveToDb() {
//        System.out.println("Testing");
//
//        return "here";
//    }
}
