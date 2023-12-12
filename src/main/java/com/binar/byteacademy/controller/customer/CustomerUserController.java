package com.binar.byteacademy.controller.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.CoursePats.CUSTOMER_USER_PATS;

@RestController
@RequestMapping(value = CUSTOMER_USER_PATS, produces = "application/json")
@RequiredArgsConstructor
public class CustomerUserController {

}
