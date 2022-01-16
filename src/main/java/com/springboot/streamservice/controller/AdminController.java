package com.springboot.streamservice.controller;

import com.springboot.streamservice.bean.AuthenticationRequest;
import com.springboot.streamservice.bean.AuthenticationResponse;
import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.UserBean;
import com.springboot.streamservice.service.CommonService;
import com.springboot.streamservice.service.impl.MyUserDetailsService;
import com.springboot.streamservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommonService commonService;

    @PostMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect Username Or Password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

    @PostMapping(value = "/registerUser", produces = "application/json")
    public ResponseEntity<?> user(@RequestBody UserBean user) {
        return ResponseEntity.ok(commonService.registerUser(user));
    }

    @PostMapping(value = "/updateFeatured", produces = "application/json")
    public ResponseEntity<?> updateFeatured(@RequestBody List<Featured> featured) {
        return commonService.updateFeatured(featured);
    }

    @GetMapping(value = "/moveToDb", produces = "application/json")
    public ResponseEntity<?> moveToDb() {
        return ResponseEntity.ok(commonService.moveToDb());
    }

    public String getUserNameFromToken(HttpServletRequest request){
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        return username;
    }
}
