package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.request.UpdateCustomerDetailRequest;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CustomerDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final CustomerDetailRepository customerDetailRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void updateCustomerDetail(UpdateCustomerDetailRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            customerDetailRepository.findByUserId(user.getId())
                    .ifPresentOrElse(customerDetail -> {
                        customerDetail.setName(request.getName());
                        customerDetail.setCity(request.getCity());
                        customerDetail.setCountry(request.getCountry());
                        customerDetailRepository.save(customerDetail);
                    }, () -> {
                        throw new DataNotFoundException("Customer detail not found");
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update user");
        }
    }
}
