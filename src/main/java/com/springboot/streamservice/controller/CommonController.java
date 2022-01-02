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

    @GetMapping(value = "/", produces = "application/json")
    public String indexPage() {
        return "Server is Running";
    }

    @GetMapping(value = "/search", produces = "application/json")
    public String indexPage(@RequestParam(value = "name") String name,
                            @RequestParam(value = "page", required = false) String page) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        return commonService.search(name, pageNo);
    }

    @GetMapping(value = "/featured", produces = "application/json")
    public String featured() {
        return commonService.featured();
    }

    //TODO: Update Multiple entries
    @PostMapping(value = "/updateFeatured", produces = "application/json")
    public void updateFeatured(@RequestBody Featured featured) {
        commonService.updateFeatured(featured);
    }

    @GetMapping(value = "/moveToDb", produces = "application/json")
//    @Scheduled(cron = "0 * * ? * *")
    public String moveToDb() {
        System.out.println("Testing");
        commonService.moveToDb();
        return "here";
    }
}
