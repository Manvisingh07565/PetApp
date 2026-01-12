package com.petapp.backend.controller;

import com.petapp.backend.dto.OrderRequest;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest data) throws RazorpayException {

        RazorpayClient client = new RazorpayClient("rzp_test_Rz6dlunkevQSLE", "JGSTQPBpTkVlttQaiCReX5ZU");

        JSONObject options = new JSONObject();
        options.put("amount", data.getAmount() * 100); // INR to Paisa
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        com.razorpay.Order order = client.orders.create(options);

        return ResponseEntity.ok(order.toString());
    }
}