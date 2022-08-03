package com.ticket.service;

import com.ticket.client.PaymentClient;
import com.ticket.dto.NotificationDto;
import com.ticket.dto.TicketDto;
import com.ticket.dto.enums.NotificationType;
import com.ticket.exception.*;
import com.ticket.model.Passenger;
import com.ticket.model.Ticket;
import com.ticket.model.Trip;
import com.ticket.model.User;
import com.ticket.model.enums.Gender;
import com.ticket.model.enums.UserType;
import com.ticket.repository.PassengerRepository;
import com.ticket.repository.TicketRepository;
import com.ticket.repository.TripRepository;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ModelMapper modelMapper;
    private final PaymentClient paymentClient;
    private final RabbitTemplate rabbitTemplate;
    private final PassengerRepository passengerRepository;


    public ResponseEntity<List<TicketDto>> buyTickets(List<TicketDto> ticketDtos, String userEmail, Long tripId) {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
        List<Ticket> boughtTickets = trip.getTickets().stream().filter(ticket -> ticket.getUser() == user).toList();
        int boughtAndWillBeCount = boughtTickets.size() + ticketDtos.size();
        List<TicketDto> purchased = new ArrayList<>();
        if (user.getUserType() == UserType.CORPORATE && boughtAndWillBeCount <= 20) { //Alınmış ve alınacak biletlerin toplamı 20ye eşit veya azmı ?
            return saveAndResponse(ticketDtos, user, trip, purchased, tripId);
        } else if (user.getUserType() == UserType.CORPORATE && boughtAndWillBeCount >= 20)
            throw new TicketBuyCondidationsException("Kurumsal kullanıcılar bir seferden 20 den fazla bilet alamazlar.");
        else if (user.getUserType() == UserType.INDIVIDUAL && boughtAndWillBeCount <= 5) { // Alınmış ve alınacak biletlerin toplamı 5e eşit veya azmı ?
            if (ticketDtos.stream().filter(ticketDto -> ticketDto.getPassenger().getGender() == Gender.MALE).count() <= 2) {//aynı anda alınan biletlerde 2 den fazla erkek varmı ?
                return saveAndResponse(ticketDtos, user, trip, purchased, tripId);
            } else
                throw new TicketBuyCondidationsException("Bireysel kullanıcılar bir siparişte en fazla 2 erkek yolcu için bilet alabilir");
        } else if (user.getUserType() == UserType.INDIVIDUAL && boughtAndWillBeCount > 5) {
            throw new TicketBuyCondidationsException("Bireysel kullanıcılar bir seferden 5 den fazla bilet alamazlar.");
        } else
            throw new TicketBuyCondidationsException("Bişeyler yanlış gitti.");
    }

    private ResponseEntity<List<TicketDto>> saveAndResponse(List<TicketDto> ticketDtos, User user, Trip trip, List<TicketDto> purchased, Long tripId) {

        ticketDtos.forEach(ticketDto -> {
            if (!trip.getPurchasedSeats().contains(ticketDto.getSeatNo())) {
                if (ticketDto.getSeatNo() <= trip.getSeatCapacity() && ticketDto.getSeatNo() > 0) {
                    trip.getPurchasedSeats().add(ticketDto.getSeatNo());
                    var paymentDto = ticketDto.getPayment();
                    paymentDto.setUserEmail(user.getEmail());
                    paymentDto.setPaymentTime(LocalDateTime.now());
                    paymentDto.setAmount(trip.getTicketPrice());
                    paymentDto.setSeatNo(ticketDto.getSeatNo());
                    paymentDto.setTripId(tripId);

                    //ödeme yap
                    var payment = paymentClient.createOrUpdatePayment(paymentDto).getBody();
                    var ticket = modelMapper.map(ticketDto, Ticket.class);
                    ticket.setTrip(trip);
                    ticket.setUser(user);
                    // passengerin id si yoksa kaydetip ekle yok sa yeni passerger kaydet
                    if (ticketDto.getPassenger().getId() == null) {
                        var passenger = passengerRepository.save(ticket.getPassenger());
                        ticket.setPassenger(passenger);
                    } else ticket.setPassenger(modelMapper.map(ticketDto.getPassenger(), Passenger.class));

                    //yapılan ödemeyi dtoya aktar
                    var savedTicketDto = modelMapper.map(ticketRepository.save(ticket), TicketDto.class);
                    savedTicketDto.setPayment(payment);
                    purchased.add(savedTicketDto);

                    // mesaj gönder
                    rabbitTemplate.convertAndSend("ticketMaster", "ticketMaster",
                            NotificationDto.builder()
                                    .sendingTime(LocalDateTime.now().toString())
                                    .to(ticketDto.getPassenger().getPhoneNumber())
                                    .from("444 0 444")
                                    .type(NotificationType.SMS)
                                    .title("")
                                    .text(trip.getFromStation().toString() + " " + trip.getToStation().toString() + " seferinden biletiniz satın alınmıştır.")
                                    .build()
                    );

                } else throw new SeatNotFoundException();
            } else {
                throw new AlreadySoldSeatException(ticketDto.getSeatNo());
            }
        });
        return ResponseEntity.ok().body(purchased.stream().map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

}
