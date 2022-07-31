package com.ticket.service;

import com.ticket.client.PaymentClient;
import com.ticket.dto.PaymentDto;
import com.ticket.dto.TripDto;
import com.ticket.model.Trip;
import com.ticket.model.User;
import com.ticket.model.enums.Role;
import com.ticket.model.enums.Station;
import com.ticket.model.enums.Vehicle;
import com.ticket.repository.TripRepository;
import com.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PaymentClient paymentClient;

    public ResponseEntity<List<TripDto>> getTripByPropertiesOrAll(Vehicle vehicle, Station to, Station from, LocalDateTime arrivalTime, LocalDateTime departureTime) {
        return ResponseEntity.ok().body(tripRepository.findTripsByProperties(arrivalTime, departureTime, vehicle, to, from).stream().map(trip -> modelMapper.map(trip, TripDto.class)).toList());
    }


    public ResponseEntity<TripDto> addTrip(TripDto request, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu maile kayıtlı bir kullanıcı yok."));
        if (user.getRole() == Role.ADMIN) {
            Trip trip = modelMapper.map(request, Trip.class);
            if (request.getVehicle() == Vehicle.BUS) trip.setSeatCapacity(45);
            else if (request.getVehicle() == Vehicle.PLANE) trip.setSeatCapacity(189);

            return ResponseEntity.ok().body(modelMapper.map(tripRepository.save(trip), TripDto.class));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sefer eklemek için yetkiniz yok.");
    }

    /**
     * @param tripId    Sefer idsi,
     * @param email kullanıcı emaili,
     * @return İşlemin sonucunu döndürür.
     * @apiNote Kullanıcının yetkisini kontrol eder daha sonra sefer idsini kontrol ettikten sonra satın alınan biletlerin ödemelerini geri ödenecek(chargeBack)
     * şeklide işaretler.
     */
    public ResponseEntity<String> cancelTrip(Long tripId, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu maile kayıtlı bir kullanıcı yok."));
        if (user.getRole() == Role.ADMIN) {
            tripRepository.findById(tripId).ifPresent(trip -> {
                trip.setIsCanceled(Boolean.TRUE);
                trip.getTickets().forEach(ticket -> {
                    //todo burayı düşün
                    List<PaymentDto> paymentDtoList = Objects.requireNonNull(paymentClient.getPaymentByEmail(ticket.getUser().getEmail()).getBody());
                    paymentDtoList.forEach(paymentDto -> paymentDto.setIsCanceled(Boolean.TRUE));
                });
            });
        } else if (user.getRole() != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yetkiniz dahilinde değil.");
        return ResponseEntity.ok().body(tripId + " numaralı sefer iptal edildi.Ve biletlerin ödemeleri geri ödenecek şeklinde işaretlendi.");
    }

    public ResponseEntity<TripDto> getTripById(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Böyle bir sefer yok."));
        return ResponseEntity.ok().body(modelMapper.map(trip, TripDto.class));
    }
}
