package com.example.posapplicationapis.services.customer;

import com.example.posapplicationapis.dto.customer.CustomerDtoRequest;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import com.example.posapplicationapis.entities.Customer;
import com.example.posapplicationapis.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDtoResponse createCustomer(CustomerDtoRequest customerDtoRequest) {
        Customer customer = new Customer();
        customer.setName(customerDtoRequest.getName());
        customer.setTarifSpecial(customerDtoRequest.getTarifSpecial());
        customer.setRemise(customerDtoRequest.getRemise());
        customerRepository.save(customer);
        return mapToDto(customer);
    }

    @Override
    public CustomerDtoResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        return mapToDto(customer);
    }

    @Override
    public List<CustomerDtoResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDtoResponse updateCustomer(Long id, CustomerDtoRequest customerDtoRequest) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setName(customerDtoRequest.getName());
        customer.setTarifSpecial(customerDtoRequest.getTarifSpecial());
        customer.setRemise(customerDtoRequest.getRemise());
        customerRepository.save(customer);
        return mapToDto(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDtoResponse mapToDto(Customer customer) {
        CustomerDtoResponse customerDto = new CustomerDtoResponse();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setTarifSpecial(customer.getTarifSpecial());
        customerDto.setRemise(customer.getRemise());
        return customerDto;
    }
}
