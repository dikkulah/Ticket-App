package com.admin.service;

import com.admin.client.PaymentClient;
import com.admin.dto.AdminDto;
import com.admin.dto.PaymentDto;
import com.admin.dto.TripDto;
import com.admin.exception.MailAlreadyInUseException;
import com.admin.exception.UserNotFoundException;
import com.admin.exception.WrongPasswordException;
import com.admin.model.Admin;
import com.admin.model.Trip;
import com.admin.model.enums.Station;
import com.admin.model.enums.Vehicle;
import com.admin.repository.AdminRepository;
import com.admin.repository.TicketRepository;
import com.admin.repository.TripRepository;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("admin")
@Slf4j
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final TripRepository tripRepository;
    private final TicketRepository ticketRepository;
    private final PaymentClient paymentClient;

    public ResponseEntity<String> register(AdminDto adminDto) {
        adminRepository.findAdminByEmail(adminDto.getEmail()).ifPresent(admin -> {
            throw new MailAlreadyInUseException();
        });
        adminDto.setPassword(Hashing.sha256().hashString(adminDto.getPassword(), StandardCharsets.UTF_8).toString());
        adminRepository.save(modelMapper.map(adminDto, Admin.class));
        return ResponseEntity.ok().body("Kaydınız başarı ile tamamlandı.");
    }

    public ResponseEntity<String> login(String email, String password) {
        Admin admin = adminRepository.findAdminByEmail(email).orElseThrow(UserNotFoundException::new);
        if (Objects.equals(admin.getPassword(), Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())) {
            return ResponseEntity.ok().body("Oturum başarıyla açıldı.");
        } else throw new WrongPasswordException();
    }

    public ResponseEntity<TripDto> addTrip(TripDto request, String email) {
        adminRepository.findAdminByEmail(email).orElseThrow(UserNotFoundException::new);
        Trip trip = modelMapper.map(request, Trip.class);
        if (request.getVehicle() == Vehicle.BUS) trip.setSeatCapacity(45);
        else if (request.getVehicle() == Vehicle.PLANE) trip.setSeatCapacity(189);
        return ResponseEntity.ok().body(modelMapper.map(tripRepository.save(trip), TripDto.class));
    }

    public ResponseEntity<String> getTotalAndCounts(String email) {
        adminRepository.findAdminByEmail(email).orElseThrow(UserNotFoundException::new);
        long count = ticketRepository.count();
        return ResponseEntity.ok().body("Toplam bilet sayısı: " + count + "\n"
                + "Topam gelir: " + ticketRepository.totalPrice());

    }

    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(Vehicle vehicle, Station to, Station
            from, LocalDateTime arrivalTime, LocalDateTime departureTime) {
        List<Trip> trips = tripRepository.findTripsByProperties(arrivalTime, departureTime, vehicle, to, from);
        return ResponseEntity.ok().body(trips.stream().map(trip -> modelMapper.map(trip, TripDto.class)).toList());
    }

    public ResponseEntity<String> cancelTrip(Long tripId, String email) {
        adminRepository.findAdminByEmail(email).orElseThrow(UserNotFoundException::new);
        Trip trip = tripRepository.findById(tripId).orElseThrow();
        trip.setIsCanceled(Boolean.TRUE);
        tripRepository.save(trip);
        if (trip.getTickets().isEmpty()) {
            log.info("hiç biley yok.");
            return ResponseEntity.badRequest().body("Sefer iptal edildi,seferden hiç bilet satın alınmamış.");
        } else {
            trip.getTickets().forEach(ticket -> {
                List<PaymentDto> paymentDtoList = paymentClient.getPaymentsOfTrip(tripId).getBody();
                paymentDtoList.forEach(paymentDto -> {
                    paymentDto.setIsCanceled(Boolean.TRUE);
                    log.info(paymentDto.toString() + " iptal edildi");
                    paymentClient.createOrUpdatePayment(paymentDto);
                });
            });
        }
        return ResponseEntity.ok().body(tripId + " numaralı sefer iptal edildi.Ve biletlerin ödemeleri geri ödenecek şeklinde işaretlendi.");
    }
}
