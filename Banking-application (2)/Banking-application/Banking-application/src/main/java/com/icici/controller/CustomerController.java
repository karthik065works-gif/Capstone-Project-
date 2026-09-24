package com.icici.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dto.CustomerPatchRequest;
import com.icici.dto.CustomerRequest;
import com.icici.dto.CustomerResponse;
import com.icici.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {

        this.customerService =
                customerService;
    }

    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse customer =
                customerService.createCustomer(
                        request
                );

        return ResponseEntity.ok(customer);
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<CustomerResponse>>
    getAllCustomers() {

        List<CustomerResponse> customers =
                customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse>
    getCustomerById(
            @PathVariable Long id) {

        CustomerResponse customer =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(customer);
    }

    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse>
    updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse customer =
                customerService.updateCustomer(
                        id,
                        request
                );

        return ResponseEntity.ok(customer);
    }

    // =========================================================
    // PATCH CUSTOMER
    // =========================================================

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse>
    patchCustomer(
            @PathVariable Long id,
            @RequestBody CustomerPatchRequest request) {

        CustomerResponse customer =
                customerService.patchCustomer(
                        id,
                        request
                );

        return ResponseEntity.ok(customer);
    }

    // =========================================================
    // DEACTIVATE CUSTOMER
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerResponse>
    deactivateCustomer(
            @PathVariable Long id) {

        CustomerResponse customer =
                customerService.deactivateCustomer(
                        id
                );

        return ResponseEntity.ok(customer);
    }

    // =========================================================
    // ACTIVATE CUSTOMER
    // =========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse>
    activateCustomer(
            @PathVariable Long id) {

        CustomerResponse customer =
                customerService.activateCustomer(
                        id
                );

        return ResponseEntity.ok(customer);
    }
}