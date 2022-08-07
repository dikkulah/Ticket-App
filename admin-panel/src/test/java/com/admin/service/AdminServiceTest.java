package com.admin.service;

import com.admin.client.PaymentClient;
import com.admin.dto.AdminDto;
import com.admin.dto.PaymentDto;
import com.admin.dto.TripDto;
import com.admin.exception.MailAlreadyInUseException;
import com.admin.exception.UserNotFoundException;
import com.admin.exception.WrongPasswordException;
import com.admin.model.Admin;
import com.admin.model.Ticket;
import com.admin.model.Trip;
import com.admin.model.enums.Station;
import com.admin.model.enums.Vehicle;
import com.admin.repository.AdminRepository;
import com.admin.repository.TicketRepository;
import com.admin.repository.TripRepository;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PaymentClient paymentClient;

    @Test
    void cant_register_because_email_inuse() {
        AdminDto request = new AdminDto();
        request.setPassword("123456");
        request.setEmail("");

        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.of(mock(Admin.class))).thenThrow(MailAlreadyInUseException.class);
        verify(adminRepository, never()).save(any(Admin.class));

        assertThrows(MailAlreadyInUseException.class, () -> adminService.register(request));
    }
    @Test
    void register_succeeded() {
        AdminDto request = new AdminDto();
        request.setPassword("123456");
        request.setEmail("");
        var admin = modelMapper.map(request, Admin.class);

        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.empty());
        var response = adminService.register(request);
        verify(adminRepository, times(1)).save(admin);


        assertEquals(ResponseEntity.ok().body("Kaydınız başarı ile tamamlandı."),response);
    }

    @Test
    void login_fail_because_admin_not_found() {
        var email = "ufuk@gmail.com";
        var password = "123123";
        when(adminRepository.findAdminByEmail(email)).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> adminService.login(email, password));
    }
    @Test
    void login_fail_because_wrong_password() {
        var email = "ufuk@gmail.com";
        var password = "123123";
        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.of(mock(Admin.class)));
        assertThrows(WrongPasswordException.class, () -> adminService.login(email, password));
    }
    @Test
    void login_succeeded() {
        var email = "ufuk@gmail.com";
        var password = "123123";

        Admin admin = new Admin();
        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.of(admin));
        admin.setPassword(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
        assertEquals(adminService.login(email, password), ResponseEntity.ok().body("Oturum başarıyla açıldı."));
    }

    @Test // body null dönüyor
    void add_trip_bus() {
        var email = "ufuk@gmail.com";
        var password = "123123";
        Admin admin = new Admin();
        TripDto tripDto = new TripDto();
        tripDto.setVehicle(Vehicle.BUS);
        tripDto.setId(1L);
        var trip= new Trip();
        trip.setVehicle(Vehicle.BUS);
        tripDto.setId(1L);

        when(modelMapper.map(tripDto, Trip.class)).thenReturn(trip);
        when(adminRepository.findAdminByEmail(email)).thenReturn(Optional.of(admin));
        when(tripRepository.save(trip)).thenReturn(trip);

        admin.setPassword(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
        var response = adminService.addTrip(tripDto,email);


        assertEquals(HttpStatus.OK, response.getStatusCode());


    }
    @Test
    void add_trip_plane() {
        var email = "ufuk@gmail.com";
        var password = "123123";
        Admin admin = new Admin();
        TripDto tripDto = new TripDto();
        tripDto.setVehicle(Vehicle.PLANE);
        tripDto.setId(1L);
        var trip= new Trip();
        trip.setVehicle(Vehicle.PLANE);
        tripDto.setId(1L);

        when(modelMapper.map(tripDto, Trip.class)).thenReturn(trip);
        when(adminRepository.findAdminByEmail(email)).thenReturn(Optional.of(admin));
        when(tripRepository.save(trip)).thenReturn(trip);

        admin.setPassword(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
        var response = adminService.addTrip(tripDto,email);


        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void get_total_and_counts() {
        var email = "ufuk@gmail.com";

        Admin admin = new Admin();
        Long count = 1L;
        when(tripRepository.count()).thenReturn(count);
        when(ticketRepository.totalPrice()).thenReturn(BigDecimal.ONE);
        when(adminRepository.findAdminByEmail(email)).thenReturn(Optional.of(admin));
        var response = adminService.getTotalAndCounts(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());


    }


    @Test
    void getTripByPropertiesOrAll() {
        List<Trip> trips= new ArrayList<>();
        Trip trip   = new Trip();
        var time = LocalDateTime.now();
        when(tripRepository.findTripsByProperties(time,time,Vehicle.BUS, Station.ANKARA,Station.ANKARA)).thenReturn(trips);
        when(modelMapper.map(trip, TripDto.class)).thenReturn(new TripDto());

        var response= adminService.getTripByPropertiesOrAll(Vehicle.BUS,Station.ANKARA,Station.ANKARA,time,time);
        assertEquals(trips.stream().map(trip1 -> modelMapper.map(trip1, TripDto.class)).toList(),response.getBody());



    }

    @Test
    void cancelTrip_and_no_tickets_sold() {
    }

    @Test
    void cancelTrip() {
        var email = "ufuk@gmail.com";
        Admin admin = new Admin();
        Trip trip = new Trip();
        Ticket ticket= new Ticket();

        trip.getTickets().add(ticket);
        var listpayment= new ArrayList<PaymentDto>();
        when(paymentClient.getPaymentsOfTrip(1L)).thenReturn(ResponseEntity.ok().body(listpayment));
        when(paymentClient.createOrUpdatePayment(PaymentDto.builder().build())).thenReturn(ResponseEntity.ok().body(PaymentDto.builder().build()));
        when(adminRepository.findAdminByEmail(email)).thenReturn(Optional.of(admin));
        when(tripRepository.save(trip)).thenReturn(trip);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        var response = adminService.cancelTrip(1L,email);

        assertEquals(1L + " numaralı sefer iptal edildi.Ve biletlerin ödemeleri geri ödenecek şeklinde işaretlendi.",response.getBody());
    }
}