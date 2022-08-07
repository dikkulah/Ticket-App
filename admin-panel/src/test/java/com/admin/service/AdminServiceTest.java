package com.admin.service;

import com.admin.client.PaymentClient;
import com.admin.dto.AdminDto;
import com.admin.exception.MailAlreadyInUseException;
import com.admin.model.Admin;
import com.admin.repository.AdminRepository;
import com.admin.repository.TicketRepository;
import com.admin.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
    }
    @Test
    void login_fail_because_wrong_password() {
    }

    @Test
    void add_trip_bus() {
    }
    @Test
    void add_trip_plane() {
    }


    @Test
    void get_total_and_counts() {
    }
    @Test
    void get_total_and_counts_user_not_found() {
    }

    @Test
    void getTripByPropertiesOrAll() {
    }

    @Test
    void cancelTrip_and_no_tickets_sold() {
    }

    @Test
    void cancelTrip() {
    }
}