package com.example.posapplicationapis.restcontroller.customer;

import com.example.posapplicationapis.dto.customer.CustomerDtoRequest;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import com.example.posapplicationapis.services.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add-customer")
    public ResponseEntity<CustomerDtoResponse> createCustomer(@RequestBody CustomerDtoRequest customerDtoRequest) {
        CustomerDtoResponse createdCustomer = customerService.createCustomer(customerDtoRequest);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping("/get-customer/{id}")
    public ResponseEntity<CustomerDtoResponse> getCustomerById(@PathVariable Long id) {
        CustomerDtoResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CustomerDtoResponse>> getAllCustomers() {
        List<CustomerDtoResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDtoResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerDtoRequest customerDtoRequest) {
        CustomerDtoResponse updatedCustomer = customerService.updateCustomer(id, customerDtoRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
