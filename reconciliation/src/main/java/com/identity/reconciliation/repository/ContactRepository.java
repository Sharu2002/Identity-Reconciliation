package com.identity.reconciliation.repository;


import com.identity.reconciliation.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Integer> {

    List<ContactEntity> findByEmailOrPhoneNumber(String email, String phoneNumber);
}