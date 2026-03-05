package com.responsys.data.repository;

import com.responsys.data.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByStatus(String status, Pageable pageable);
    Optional<Contact> findByEmail(String email);
}
