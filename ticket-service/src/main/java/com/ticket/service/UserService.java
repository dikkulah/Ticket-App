package com.ticket.service;

import com.google.common.hash.Hashing;
import com.ticket.dto.MailDto;
import com.ticket.dto.NotificationDto;
import com.ticket.dto.TicketDto;
import com.ticket.dto.UserDto;
import com.ticket.model.User;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AmqpTemplate amqpTemplate;

    public ResponseEntity<String> register(UserDto request) {
        userRepository.findUserByEmail(request.getEmail()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu mail daha önce kullanılmış.");
        });
        request.setPassword(Hashing.sha256().hashString(request.getPassword(), StandardCharsets.UTF_8).toString());
        userRepository.save(modelMapper.map(request, User.class));

        NotificationDto notificationDto = new MailDto("Kaydınız başarıyla gerçekleşmiştir.", "Ticket Master", request.getEmail(), "ticketmaster@gmail.com", LocalDateTime.now().toString());
        amqpTemplate.convertAndSend(notificationDto);
        return ResponseEntity.ok().body(request.getEmail() + " mailli kullanıcı başarı ile kayıt edildi");
    }

    public ResponseEntity<String> login(String email, String password) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bu maile kayıtlı bir kullanıcı bulunamadı."));
        return Objects.equals(user.getPassword(), Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())
                ? ResponseEntity.ok().body("Oturum başarıyla açıldı.")
                : ResponseEntity.badRequest().body("Şifre yanlış");
    }

    public ResponseEntity<List<TicketDto>> getTicketsByUserId(Long id) {
        return ResponseEntity.ok().body(userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Böyle bir kullanıcı mevcut değil."))
                .getTickets().stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    public ResponseEntity<UserDto> getByUserEmail(String email) {
        User user= userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Böyle bir kullanıcı mevcut değil"));
        return ResponseEntity.ok().body(modelMapper.map(user,UserDto.class));
    }

    public ResponseEntity<String> deleteByUserEmail(String email) {
        User user= userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Böyle bir kullanıcı mevcut değil"));
        userRepository.delete(user);
        return ResponseEntity.ok().body(email+" kullanıcısı başarıyla silindi");
    }
}
