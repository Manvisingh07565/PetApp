package com.petapp.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "total_amount", nullable = false)
    private Double amount; // Java mein 'amount' hi rahega par DB mein 'total_amount' jayega

    @Column(name = "payment_method", nullable = false)
    private String method; // Java mein 'method' hi rahega par DB mein 'payment_method' jayega

    @Column(name = "delivery_address", nullable = false)
    private String address; // Java mein 'address' hi rahega par DB mein 'delivery_address' jayega

    @Column(columnDefinition = "LONGTEXT")
    private String items;

    @Column(name = "order_status", nullable = false)
    private String status; // Java mein 'status' hi rahega par DB mein 'order_status' jayega

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING"; // Naya field jo DB ko chahiye

    private LocalDateTime orderDate;
}