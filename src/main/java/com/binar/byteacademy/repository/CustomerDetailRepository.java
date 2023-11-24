package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, UUID> {

}
