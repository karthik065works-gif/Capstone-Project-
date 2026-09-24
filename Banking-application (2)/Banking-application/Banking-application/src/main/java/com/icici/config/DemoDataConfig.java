package com.icici.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.icici.model.Customer;
import com.icici.model.CustomerStatus;
import com.icici.repository.CustomerRepository;

@Configuration
public class DemoDataConfig {

    @Bean
    CommandLineRunner loadDemoData(CustomerRepository customerRepository) {

        return args -> {

            // Do not insert demo data if customers already exist
            if (customerRepository.count() > 0) {
                return;
            }

            Customer customer1 = new Customer();

            customer1.setName("Rahul Sharma");
            customer1.setEmail("rahul.sharma@example.com");
            customer1.setPhone("9876543210");
            customer1.setAddress("Mumbai, Maharashtra");
            customer1.setStatus(CustomerStatus.ACTIVE);

            customerRepository.save(customer1);

            Customer customer2 = new Customer();

            customer2.setName("Priya Nair");
            customer2.setEmail("priya.nair@example.com");
            customer2.setPhone("9876543211");
            customer2.setAddress("Thane, Maharashtra");
            customer2.setStatus(CustomerStatus.ACTIVE);

            customerRepository.save(customer2);

            Customer customer3 = new Customer();

            customer3.setName("Arjun Mehta");
            customer3.setEmail("arjun.mehta@example.com");
            customer3.setPhone("9876543212");
            customer3.setAddress("Bengaluru, Karnataka");
            customer3.setStatus(CustomerStatus.ACTIVE);

            customerRepository.save(customer3);

            System.out.println("======================================");
            System.out.println("DEMO DATA INSERTED");
            System.out.println("Customer 1: Rahul Sharma");
            System.out.println("Customer 2: Priya Nair");
            System.out.println("Customer 3: Arjun Mehta");
            System.out.println("======================================");
        };
    }
}