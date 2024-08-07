package com.example.posapplicationapis.services.customer;

import com.example.posapplicationapis.dto.customer.CustomerDtoRequest;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;

import java.util.List;

public interface CustomerService {

    CustomerDtoResponse createCustomer(CustomerDtoRequest customerDtoRequest);

    CustomerDtoResponse getCustomerById(Long id);

    List<CustomerDtoResponse> getAllCustomers();

    CustomerDtoResponse updateCustomer(Long id, CustomerDtoRequest customerDtoRequest);

    void deleteCustomer(Long id);
}