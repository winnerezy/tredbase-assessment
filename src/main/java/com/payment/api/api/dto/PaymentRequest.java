package com.payment.api.api.dto;

public record PaymentRequest(Long parentId, Long studentId, Double paymentAmount) {}