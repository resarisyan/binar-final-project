package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.UpdateCustomerDetailRequest;
import com.binar.byteacademy.dto.response.UserAdminResponse;
import com.binar.byteacademy.dto.response.UserCustomerResponse;

import java.security.Principal;

public interface UserService {
    void updateCustomerDetail(UpdateCustomerDetailRequest request, Principal connectedUser);
    UserAdminResponse getAdminDetail(Principal connectedUser);
    UserCustomerResponse getCustomerDetail(Principal connectedUser);
}
