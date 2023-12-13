package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.UpdateCustomerDetailRequest;

import java.security.Principal;

public interface UserService {
    void updateCustomerDetail(UpdateCustomerDetailRequest request, Principal connectedUser);
}
