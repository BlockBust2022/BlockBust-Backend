package com.springboot.streamservice.controller;

import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.constants.StreamConstants;
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
@CrossOrigin(origins = "http://localhost:3000")
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
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

    @GetMapping(value = "/getFeatured", produces = "application/json")
    public ResponseEntity<?> getFeatured() {
        return commonService.getFeatured();
    }

    @PostMapping(value = "/updateFeatured", produces = "application/json")
    public ResponseEntity<?> updateFeatured(@RequestBody Featured featured) {
        return commonService.updateFeatured(featured, StreamConstants.INSERT);
    }

    @DeleteMapping(value = "/updateFeatured", produces = "application/json")
    public ResponseEntity<?> DeleteFeatured(@RequestBody Featured featured) {
        return commonService.updateFeatured(featured, StreamConstants.DELETE);
    }


    @GetMapping(value = "/moveToDb", produces = "application/json")
    public ResponseEntity<?> moveToDb() {
        return ResponseEntity.ok(commonService.moveToDb());
    }

    @GetMapping(value = "/getMoviesList", produces = "application/json")
    public ResponseEntity<?> getMovies(@RequestParam(value = "page", required = false) String page,
                                       @RequestParam(value = "limit", required = false) String limit) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        int limitNo = null != limit && (Integer.parseInt(limit) > 20) ? Integer.parseInt(limit) : 20;
        return commonService.getStreamData(pageNo, limitNo);
    }

    @PostMapping(value = "/updateMovies", produces = "application/json")
    public ResponseEntity<?> insertMovies(@RequestBody Stream stream) {
        return commonService.updateStreamData(stream, StreamConstants.INSERT);
    }

    @PutMapping(value = "/updateMovies", produces = "application/json")
    public ResponseEntity<?> updateMovies(@RequestBody Stream stream) {
        return commonService.updateStreamData(stream, StreamConstants.UPDATE);
    }

    @DeleteMapping(value = "/updateMovies", produces = "application/json")
    public ResponseEntity<?> DeleteMovies(@RequestBody Stream stream) {
        return commonService.updateStreamData(stream, StreamConstants.DELETE);
    }

}
