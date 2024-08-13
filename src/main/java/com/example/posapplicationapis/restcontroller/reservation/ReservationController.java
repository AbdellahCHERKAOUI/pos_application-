package com.example.posapplicationapis.restcontroller.reservation;

import com.example.posapplicationapis.dto.reservation.ReservationDtoRequest;
import com.example.posapplicationapis.entities.Cashier;
import com.example.posapplicationapis.entities.Customer;
import com.example.posapplicationapis.entities.Reservation;
import com.example.posapplicationapis.entities.Table;
import com.example.posapplicationapis.repositories.CashierRepository;
import com.example.posapplicationapis.repositories.CustomerRepository;
import com.example.posapplicationapis.repositories.ReservationRepository;
import com.example.posapplicationapis.repositories.TableRepository;
import com.example.posapplicationapis.services.reservation.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation/")
public class ReservationController {

    @Autowired
    private ReservationServiceImpl reservationService;

    @PostMapping("/make-reservation/{clientId}/{cashierId}")
    public String makeReservation(@PathVariable Long clientId,
                                  @PathVariable Long cashierId,
                                  @RequestBody ReservationDtoRequest reservationDtoRequest) {
        return reservationService.make(clientId,cashierId,reservationDtoRequest);
    }

    @PostMapping("/cancel-reservation/{clientId}/{cashierId}")
    public String cancelReservation(@PathVariable Long clientId,
                                  @PathVariable Long cashierId,
                                  @RequestBody ReservationDtoRequest reservationDtoRequest) {
        return reservationService.cancel(clientId,cashierId,reservationDtoRequest);
    }
}
