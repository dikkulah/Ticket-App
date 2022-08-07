package com.ticket.controller;

import com.ticket.dto.TicketDto;
import com.ticket.dto.UserDto;
import com.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * @param request Userın bilgilerini alır
     *                Şifreyi hashleyerek kaydeder.
     * @return Kaydederken oluşan duruma göre String döndürür.
     */
    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDto request) {
        log.info("user service , register");
        return userService.register(request);
    }

    /**
     * Sahte bir login işlemi yapar. Önce email in kayıtlı olup olmadığını daha sonra göderilern şifre hashleyerek
     * databaseden gelen şifre ile eşleştirerek işlemin başarısına göre string döndürür.
     * @param email user email
     * @param password şifre
     * @return işlem sonucu
     */
    @PostMapping("{email}/{password}")
    public ResponseEntity<String> login(@PathVariable String email, @PathVariable String password) {
        log.info("user service , login");
        return userService.login(email, password);
    }

    /**
     * @param id userid
     * @return Kullanıncının aldığı biletleri listeler
     */
    @GetMapping("tickets/{id}")
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(@PathVariable Long id) {
        log.info("user service , getTicketsByUserId");
        return userService.getTicketsByUserId(id);
    }

    @GetMapping("{email}")
    public ResponseEntity<UserDto> getByUserEmail(@PathVariable String email) {
        log.info("user service , getByUserEmail");
        log.info(LocalDateTime.parse("2017-01-13 17:09").toString());

        return userService.getByUserEmail(email);
    }
    @DeleteMapping("{email}")
    public ResponseEntity<String> deleteByUserEmail(@PathVariable String email) {
        log.info("user service , deleteByUserEmail");
        return userService.deleteByUserEmail(email);
    }



}
