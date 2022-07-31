package com.ticket.service;

import com.ticket.client.PaymentClient;
import com.ticket.dto.NotificationDto;
import com.ticket.dto.SmsDto;
import com.ticket.dto.TicketDto;
import com.ticket.model.Ticket;
import com.ticket.model.Trip;
import com.ticket.model.User;
import com.ticket.model.enums.Gender;
import com.ticket.model.enums.Role;
import com.ticket.model.enums.UserType;
import com.ticket.repository.TicketRepository;
import com.ticket.repository.TripRepository;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private static final String USER_NOT_FOUND = "Bu maile kayıtlı bir kullanıcı mevcut değil.";
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ModelMapper modelMapper;
    private final PaymentClient paymentClient;
    private final AmqpTemplate amqpTemplate;


    public ResponseEntity<String> getTotalAndCounts(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        if (user.getRole() == Role.ADMIN) {
            long count = ticketRepository.count();
            BigDecimal totalPrice = ticketRepository.totalPrice();
            return ResponseEntity.ok().body("Toplam bilet sayısı: " + count + "\n"
                    + "Topam gelir: " + totalPrice);
        } else if (user.getRole() != Role.ADMIN) {
            return ResponseEntity.badRequest().body("Yetkiniz dahilinde değil.");
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hesaplanamadı.");
    }

    public ResponseEntity<List<TicketDto>> buyTickets(List<TicketDto> ticketDtos, String email, Long tripId) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Böyle bir sefer mevcut değil."));
        long boughtAndWillBeCount = trip.getTickets().stream().filter(ticket -> ticket.getUser() == user).count() + ticketDtos.size();
        List<TicketDto> purchased = new ArrayList<>();

        if (user.getRole() == Role.ADMIN) {
            return saveAndResponse(ticketDtos, user, trip, purchased,tripId);
        } else if (user.getUserType() == UserType.CORPORATE && boughtAndWillBeCount <= 20) { //Alınmış ve alınack biletlerin toplamı 20ye eşit veya azmı ?
            return saveAndResponse(ticketDtos, user, trip, purchased, tripId);
        } else if (user.getUserType() == UserType.CORPORATE && boughtAndWillBeCount >= 20)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kurumsal kullanıcılar bir seferden 20 den fazla bilet alamazlar.");
        else if (user.getUserType() == UserType.INDIVIDUAL && boughtAndWillBeCount <= 5) { // Alınmış ve alınack biletlerin toplamı 5e eşit veya azmı ?
            if (ticketDtos.stream().map(ticketDto -> modelMapper.map(ticketDto, Ticket.class)).filter(ticket -> ticket.getGender() == Gender.MALE).count() <= 2) { //aynı anda alınan biletlerde 2 den fazla erkek varmı ?
                return saveAndResponse(ticketDtos, user, trip, purchased, tripId);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bireysel kullanıcılar tek seferde en fazla 2 Erkek yolcu için bilet alabilir.");
        } else if (user.getUserType() == UserType.INDIVIDUAL && boughtAndWillBeCount >= 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bireysel kullanıcılar bir seferden 5 den fazla bilet alamazlar.");
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "https://youtu.be/ncLtGldfIbM");
    }

    private ResponseEntity<List<TicketDto>> saveAndResponse(List<TicketDto> ticketDtos, User user, Trip trip, List<TicketDto> purchased, Long tripId) {
        ticketDtos.forEach(ticketDto -> {
            if (!trip.getPurchasedSeats().contains(ticketDto.getSeatNo())) {
                if (ticketDto.getSeatNo() <= trip.getSeatCapacity()) {
                    trip.getPurchasedSeats().add(ticketDto.getSeatNo());
                    var paymentDto = modelMapper.map(ticketDto, TicketDto.class).getPayment();
                    paymentDto.setUserEmail(user.getEmail());
                    paymentDto.setPaymentTime(LocalDateTime.now());
                    paymentDto.setAmount(ticketDto.getTicketPrice());
                    paymentDto.setSeatNo(ticketDto.getSeatNo());
                    paymentDto.setTripId(tripId);
                    paymentClient.createOrSavePayment(paymentDto).getBody();
                    var ticket= modelMapper.map(ticketDto, Ticket.class);
                    ticket.setTrip(trip);
                    ticket.setUser(user);
                    TicketDto ticketDto1 = modelMapper.map(ticketRepository.save(ticket), TicketDto.class);
                    ticketDto1.setPayment(paymentClient.getPaymentOfTicket(user.getEmail(), tripId, ticketDto.getSeatNo()).getBody());
                    purchased.add(ticketDto1);

                    NotificationDto notificationDto = new SmsDto(trip.getId() + " numaralı seferden "
                            + ticketDto.getSeatNo() + " numaralı koltuk satın almıştır "
                            + "Kalkış saati: " + trip.getDepartureTime().toString(), user.getPhoneNumber());

                    amqpTemplate.convertAndSend(notificationDto);
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu koltuk numarası mevcut değil.");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ticketDto.getSeatNo() + " numaralı koltuk daha önce satın alınmış, bu bilet satın alınamadı.");
            }
        });

        return ResponseEntity.ok().body(purchased.stream().map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }
}
