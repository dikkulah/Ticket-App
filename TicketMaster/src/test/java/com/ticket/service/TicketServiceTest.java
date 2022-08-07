package com.ticket.service;

import com.ticket.client.PaymentClient;
import com.ticket.dto.NotificationDto;
import com.ticket.dto.PassengerDto;
import com.ticket.dto.PaymentDto;
import com.ticket.dto.TicketDto;
import com.ticket.exception.TicketBuyCondidationsException;
import com.ticket.exception.TripNotFoundException;
import com.ticket.exception.UserNotFoundException;
import com.ticket.model.Passenger;
import com.ticket.model.Ticket;
import com.ticket.model.Trip;
import com.ticket.model.User;
import com.ticket.model.enums.Gender;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.UserType;
import com.ticket.repository.PassengerRepository;
import com.ticket.repository.TicketRepository;
import com.ticket.repository.TripRepository;
import com.ticket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TicketServiceTest {
    @InjectMocks
    private TicketService ticketService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private PaymentClient paymentClient;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private PassengerRepository passengerRepository;

    @Test
    void get_tickets() {
        var email = "ufuk@gmail.com";
        var list = new ArrayList<Ticket>();
        list.add(new Ticket());
        var user = new User();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketsByUser(user)).thenReturn(list);

        var response = ticketService.getTicketsByUserEmail(email);

        verify(userRepository, times(1)).findUserByEmail(email);
        verify(ticketRepository, times(1)).findTicketsByUser(user);

        assertEquals(list.stream().map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList(), response.getBody());

    }

    @Test
    void cant_get_tickets_because_user_not_found() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var passenger = new Passenger();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty()).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> ticketService.getTicketsByUserEmail(email));
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(ticketRepository, never()).findTicketsByUser(user);
        verify(ticketRepository, never()).findTicketsByUser(user);
        verify(passengerRepository, never()).save(passenger);

    }

    @Test
    void cant_buy_tickets_because_user_not_found() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        PaymentDto paymentDto = new PaymentDto();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty()).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> ticketService.getTicketsByUserEmail(email));
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, never()).findById(id);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
        verify(paymentClient, never()).createOrUpdatePayment(paymentDto);

    }

    @Test
    void cant_buy_tickets_because_trip_not_found() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        PaymentDto paymentDto = new PaymentDto();
        var listTicket = new ArrayList<TicketDto>();
        listTicket.add(new TicketDto());
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.empty()).thenThrow(TripNotFoundException.class);
        assertThrows(TripNotFoundException.class, () -> ticketService.buyTickets(listTicket, email, id));
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
        verify(paymentClient, never()).createOrUpdatePayment(paymentDto);
    }

    @Test
    void cant_buy_tickets_because_individual_buy_to_much_ticket() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setUserType(UserType.INDIVIDUAL);
        user.setEmail(email);
        var trip = new Trip();
        var emptyList = new ArrayList<Ticket>();
        var passenger = new Passenger();
        passenger.setGender(Gender.FEMALE);
        var ticket = new Ticket(id,40,trip,user,passenger);

        var listTicket = prepareTickets(5, emptyList,ticket);

        trip.setSeatCapacity(45);
        trip.setTickets(listTicket);


        var listTicketDto = new ArrayList<TicketDto>();
        listTicketDto.add(TicketDto.builder()
                        .seatNo(20)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build());

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        trip.getTickets().addAll(listTicket);
        assertThrows(TicketBuyCondidationsException.class, () -> ticketService.buyTickets(listTicketDto, email, id));

        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
        verify(paymentClient, never()).createOrUpdatePayment(any(PaymentDto.class));

    }

    @Test
    void cant_buy_tickets_because_individual_buy_to_much_ticket_gender_male_for_one_buy() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setUserType(UserType.INDIVIDUAL);
        user.setEmail(email);
        var trip = new Trip();
        var passenger = new Passenger();
        passenger.setGender(Gender.MALE);


        trip.setSeatCapacity(45);


        var listTicketDto = new ArrayList<TicketDto>();
        TicketDto dto1 = TicketDto.builder()
                .seatNo(20)
                .payment(PaymentDto.builder().build())
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build();
        TicketDto dto2 = TicketDto.builder()
                .payment(PaymentDto.builder().build())
                .seatNo(21)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build();
        TicketDto dto3 = TicketDto.builder()
                .payment(PaymentDto.builder().build())
                .seatNo(2)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build();
        listTicketDto.add(dto1);
        listTicketDto.add(dto2);
        listTicketDto.add(dto3);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        assertThrows(TicketBuyCondidationsException.class, () -> ticketService.buyTickets(listTicketDto, email, id));

        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
        verify(paymentClient, never()).createOrUpdatePayment(any(PaymentDto.class));
    }

    @Test
    void cant_buy_tickets_because_corporate_buy_to_much_ticket() {
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setUserType(UserType.CORPORATE);
        user.setEmail(email);
        var trip = new Trip();
        var emptyList = new ArrayList<Ticket>();
        var passenger = new Passenger();
        passenger.setGender(Gender.FEMALE);
        var ticket = new Ticket(id,40,trip,user,passenger);

        var listTicket = prepareTickets(20, emptyList,ticket);

        trip.setSeatCapacity(45);
        trip.setTickets(listTicket);


        var listTicketDto = new ArrayList<TicketDto>();
        listTicketDto.add(TicketDto.builder()
                .seatNo(20)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build());

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        trip.getTickets().addAll(listTicket);
        assertThrows(TicketBuyCondidationsException.class, () -> ticketService.buyTickets(listTicketDto, email, id));

        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
        verify(paymentClient, never()).createOrUpdatePayment(any(PaymentDto.class));
    }
    @Test
    void cant_buy_tickets_because_user_type_wrong(){
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setEmail(email);
        var trip = new Trip();
        var listTicketDto = new ArrayList<TicketDto>();
        listTicketDto.add(TicketDto.builder()
                .seatNo(20)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build());
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user)).thenThrow(IllegalArgumentException.class);
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));

        assertThrows(IllegalArgumentException.class, () -> ticketService.buyTickets(listTicketDto, email, id));

    }

    // body null dönüyor çözemedim
    @Test
    void buy_ticket_corporate(){
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setUserType(UserType.CORPORATE);
        user.setEmail(email);
        var trip = new Trip();
        trip.setFromStation(Station.ANKARA);
        trip.setToStation(Station.ANKARA);
        var passenger = new Passenger();
        passenger.setGender(Gender.FEMALE);


        trip.setSeatCapacity(45);

        PaymentDto paymentDto = PaymentDto.builder().build();
        var listTicketDto = new ArrayList<TicketDto>();
        listTicketDto.add(TicketDto.builder()
                        .id(id)
                .seatNo(20)
                .payment(paymentDto)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build());
        var ticketDto = listTicketDto.stream().findFirst().get();

        var ticket = new Ticket();
        ticket.setTrip(trip);
        ticket.setUser(user);
        ticket.setPassenger(passenger);
        ticket.setSeatNo(20);
        ticket.setId(id);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        when(ticketRepository.save(ticket)).thenReturn(ticket);


        when(paymentClient.createOrUpdatePayment(paymentDto)).thenReturn(ResponseEntity.ok().body(paymentDto));
        when(modelMapper.map(ticketDto, Ticket.class)).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketDto.class)).thenReturn(ticketDto);

        var response = ticketService.buyTickets(listTicketDto, email, id);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate,times(1)).convertAndSend(anyString(), anyString(), any(NotificationDto.class));
        verify(paymentClient, times(1)).createOrUpdatePayment(any(PaymentDto.class));
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void buy_ticket_individual(){
        var email = "ufuk@gmail.com";
        var user = new User();
        var id = 1L;
        user.setUserType(UserType.INDIVIDUAL);
        user.setEmail(email);
        var trip = new Trip();
        trip.setFromStation(Station.ANKARA);
        trip.setToStation(Station.ANKARA);
        var passenger = new Passenger();
        passenger.setGender(Gender.FEMALE);


        trip.setSeatCapacity(45);

        PaymentDto paymentDto = PaymentDto.builder().build();
        var listTicketDto = new ArrayList<TicketDto>();
        listTicketDto.add(TicketDto.builder()
                .id(id)
                .seatNo(20)
                .payment(paymentDto)
                .passenger(PassengerDto.builder().gender(Gender.MALE).build()).build());
        var ticketDto = listTicketDto.stream().findFirst().get();

        var ticket = new Ticket();
        ticket.setTrip(trip);
        ticket.setUser(user);
        ticket.setPassenger(passenger);
        ticket.setSeatNo(20);
        ticket.setId(id);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        when(ticketRepository.save(ticket)).thenReturn(ticket);


        when(paymentClient.createOrUpdatePayment(paymentDto)).thenReturn(ResponseEntity.ok().body(paymentDto));
        when(modelMapper.map(ticketDto, Ticket.class)).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketDto.class)).thenReturn(ticketDto);

        var response = ticketService.buyTickets(listTicketDto, email, id);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(tripRepository, times(1)).findById(id);
        verify(rabbitTemplate,times(1)).convertAndSend(anyString(), anyString(), any(NotificationDto.class));
        verify(paymentClient, times(1)).createOrUpdatePayment(any(PaymentDto.class));
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    private List<Ticket> prepareTickets(int i, List<Ticket> emptyList, Ticket ticket) {
        for (int j = 1; j <= i; j++) {
            emptyList.add(ticket);
        }
        return emptyList;

    }
}