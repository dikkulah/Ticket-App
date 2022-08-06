package com.ticket.service;

import com.google.common.hash.Hashing;
import com.ticket.dto.NotificationDto;
import com.ticket.dto.TicketDto;
import com.ticket.dto.UserDto;
import com.ticket.dto.enums.NotificationType;
import com.ticket.exception.MailAlreadyInUseException;
import com.ticket.exception.UserNotFoundException;
import com.ticket.exception.WrongPasswordException;
import com.ticket.model.User;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final RabbitTemplate rabbitTemplate;

    public ResponseEntity<String> register(UserDto request) {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent())
            throw new MailAlreadyInUseException();
        else {
            request.setPassword(Hashing.sha256().hashString(request.getPassword(), StandardCharsets.UTF_8).toString());
            userRepository.save(modelMapper.map(request, User.class));
            rabbitTemplate.convertAndSend("ticketMaster", "ticketMaster", new NotificationDto(NotificationType.EMAIL,
                    "Ticket Master",
                    request.getEmail(),
                    "service@ticketmaster.com",
                    "Kaydınız başarı ile gerçekleşti",
                    LocalDateTime.now().toString()));
            return ResponseEntity.ok().body(request.getEmail() + " mailli kullanıcı başarı ile kayıt edildi");
        }
    }
    public ResponseEntity<String> login(String email, String password) {
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        if (Objects.equals(user.getPassword(), Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())) {
            return ResponseEntity.ok().body("Oturum başarıyla açıldı.");
        }else throw new WrongPasswordException();
    }
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok().body(user
                .getTickets().stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }
    public ResponseEntity<UserDto> getByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok().body(modelMapper.map(user,UserDto.class));
    }
    public ResponseEntity<String> deleteByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
        return ResponseEntity.ok().body(email+" kullanıcısı başarıyla silindi");
    }
}
