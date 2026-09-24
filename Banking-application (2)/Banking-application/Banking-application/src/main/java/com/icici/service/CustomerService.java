package com.icici.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dto.CustomerPatchRequest;
import com.icici.dto.CustomerRequest;
import com.icici.dto.CustomerResponse;
import com.icici.exception.CustomerNotFoundException;
import com.icici.exception.DuplicateCustomerException;
import com.icici.model.Customer;
import com.icici.model.CustomerStatus;
import com.icici.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(
            CustomerRepository customerRepository) {

        this.customerRepository =
                customerRepository;
    }

    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @Transactional
    public CustomerResponse createCustomer(
            CustomerRequest request) {

        if (customerRepository
                .existsByEmail(request.getEmail())) {

            throw new DuplicateCustomerException(
                    "Customer already exists with email: "
                            + request.getEmail()
            );
        }

        if (customerRepository
                .existsByPhone(request.getPhone())) {

            throw new DuplicateCustomerException(
                    "Customer already exists with phone: "
                            + request.getPhone()
            );
        }

        Customer customer =
                new Customer();

        customer.setName(
                request.getName()
        );

        customer.setEmail(
                request.getEmail()
        );

        customer.setPhone(
                request.getPhone()
        );

        customer.setAddress(
                request.getAddress()
        );

        customer.setStatus(
                CustomerStatus.ACTIVE
        );

        customer.setCreatedAt(
                LocalDateTime.now()
        );

        Customer savedCustomer =
                customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    public List<CustomerResponse> getAllCustomers() {

        return customerRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    public CustomerResponse getCustomerById(
            Long id) {

        Customer customer =
                findCustomer(id);

        return toResponse(customer);
    }

    // =========================================================
    // UPDATE CUSTOMER - PUT
    // =========================================================

    @Transactional
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer =
                findCustomer(id);

        // Check whether email belongs to another customer
        if (!customer.getEmail()
                .equals(request.getEmail())
                && customerRepository
                .existsByEmail(
                        request.getEmail()
                )) {

            throw new DuplicateCustomerException(
                    "Customer already exists with email: "
                            + request.getEmail()
            );
        }

        // Check whether phone belongs to another customer
        if (!customer.getPhone()
                .equals(request.getPhone())
                && customerRepository
                .existsByPhone(
                        request.getPhone()
                )) {

            throw new DuplicateCustomerException(
                    "Customer already exists with phone: "
                            + request.getPhone()
            );
        }

        customer.setName(
                request.getName()
        );

        customer.setEmail(
                request.getEmail()
        );

        customer.setPhone(
                request.getPhone()
        );

        customer.setAddress(
                request.getAddress()
        );

        Customer updatedCustomer =
                customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    // =========================================================
    // PATCH CUSTOMER
    // =========================================================

    @Transactional
    public CustomerResponse patchCustomer(
            Long id,
            CustomerPatchRequest request) {

        Customer customer =
                findCustomer(id);

        if (request.getName() != null) {

            customer.setName(
                    request.getName()
            );
        }

        if (request.getEmail() != null) {

            if (!customer.getEmail()
                    .equals(request.getEmail())
                    && customerRepository
                    .existsByEmail(
                            request.getEmail()
                    )) {

                throw new DuplicateCustomerException(
                        "Customer already exists with email: "
                                + request.getEmail()
                );
            }

            customer.setEmail(
                    request.getEmail()
            );
        }

        if (request.getPhone() != null) {

            if (!customer.getPhone()
                    .equals(request.getPhone())
                    && customerRepository
                    .existsByPhone(
                            request.getPhone()
                    )) {

                throw new DuplicateCustomerException(
                        "Customer already exists with phone: "
                                + request.getPhone()
                );
            }

            customer.setPhone(
                    request.getPhone()
            );
        }

        if (request.getAddress() != null) {

            customer.setAddress(
                    request.getAddress()
            );
        }

        Customer updatedCustomer =
                customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    // =========================================================
    // DEACTIVATE CUSTOMER
    // =========================================================

    @Transactional
    public CustomerResponse deactivateCustomer(
            Long id) {

        Customer customer =
                findCustomer(id);

        customer.setStatus(
                CustomerStatus.INACTIVE
        );

        Customer updatedCustomer =
                customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    // =========================================================
    // ACTIVATE CUSTOMER
    // =========================================================

    @Transactional
    public CustomerResponse activateCustomer(
            Long id) {

        Customer customer =
                findCustomer(id);

        customer.setStatus(
                CustomerStatus.ACTIVE
        );

        Customer updatedCustomer =
                customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    // =========================================================
    // FIND CUSTOMER
    // =========================================================

    private Customer findCustomer(
            Long id) {

        return customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private CustomerResponse toResponse(
            Customer customer) {

        CustomerResponse response =
                new CustomerResponse();

        response.setId(
                customer.getId()
        );

        response.setName(
                customer.getName()
        );

        response.setEmail(
                customer.getEmail()
        );

        response.setPhone(
                customer.getPhone()
        );

        response.setAddress(
                customer.getAddress()
        );

        response.setStatus(
                customer.getStatus()
        );

        response.setCreatedAt(
                customer.getCreatedAt()
        );

        return response;
    }
}