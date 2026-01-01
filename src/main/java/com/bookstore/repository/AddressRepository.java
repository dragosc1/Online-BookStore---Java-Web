package com.bookstore.repository;

import com.bookstore.model.Address;
import com.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Find all addresses for a specific user
    List<Address> findByUser(User user);

    // Optional: Find an address by postal code
    List<Address> findByPostalCode(String postalCode);

    // Optional: Find addresses by city
    List<Address> findByCity(String city);

    // Optional: Find addresses by country
    List<Address> findByCountry(String country);
}
