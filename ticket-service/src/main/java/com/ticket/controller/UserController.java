package com.ticket.controller;

import com.ticket.dto.TicketDto;
import com.ticket.dto.UserDto;
import com.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDto request){
        return userService.register(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(@PathVariable Long id){
        return userService.getTicketsByUserId(id);
    }

    @PostMapping("{email}/{password}")
    public ResponseEntity<String> login(@PathVariable String email,@PathVariable String password){
        return userService.login(email,password);
    }


    //todo login


}
