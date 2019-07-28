package com.oocl.ita.parkinglot.controller;


import com.oocl.ita.parkinglot.annotation.Auth;
import com.oocl.ita.parkinglot.enums.RoleEnum;
import com.oocl.ita.parkinglot.model.Orders;
import com.oocl.ita.parkinglot.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping("/orders")
    public ResponseEntity getAllOrders () {
        return ResponseEntity.ok(ordersRepository.findAllNotReceiptOrders());
    }
}
