package com.example.posapplicationapis.services.reservation;

import com.example.posapplicationapis.dto.reservation.ReservationDtoRequest;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.enums.ERole;
import com.example.posapplicationapis.enums.TableStatus;
import com.example.posapplicationapis.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CashierRepository cashierRepository;

    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private UserRepository userRepository;


    public String make(Long clientId, Long cashierId, ReservationDtoRequest reservationDtoRequest) {

        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));


        User cashier = userRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cashier ID"));
        for (Role role : cashier.getRoles()) {
            if (!role.getName().equals(ERole.ROLE_CASHIER)) {
                throw new IllegalArgumentException("Invalid cashier ID");
            }
        }

        Reservation reservation = new Reservation();
        reservation.setDate(reservationDtoRequest.getDate() != null ? reservationDtoRequest.getDate() : LocalDateTime.now());
        reservation.setCustomer(customer);
        reservation.setCashier(cashier);


        List<Table> tables = tableRepository.findAllById(reservationDtoRequest.getTables());
        for (Table table : tables) {
            table.setReserved(TableStatus.RESERVED);
        }
        reservation.setTables(tables);


        reservationRepository.save(reservation);

        return "Reservation created successfully with ID: " + reservation.getId();
    }

    public String cancel(Long clientId, Long cashierId, ReservationDtoRequest reservationDtoRequest) {

        User cashier = userRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cashier ID"));
        for (Role role : cashier.getRoles()) {
            if (!role.getName().equals(ERole.ROLE_CASHIER)) {
                throw new IllegalArgumentException("Invalid cashier ID");
            }
        }

        Reservation reservation = reservationRepository.findByCustomerId(clientId).orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));


        List<Table> tables = tableRepository.findAllById(reservationDtoRequest.getTables());
        for (Table table : tables) {
            table.setReserved(TableStatus.AVAILABLE);
        }
        reservation.setTables(tables);

        reservationRepository.delete(reservation);

        return "Reservation canceled";
    }

}
