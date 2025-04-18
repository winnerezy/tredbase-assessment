package com.payment.api.api.service;

import com.payment.api.api.dto.PaymentRequest;
import com.payment.api.api.model.Parent;
import com.payment.api.api.model.Payment;
import com.payment.api.api.model.Student;
import com.payment.api.api.repository.ParentRepository;
import com.payment.api.api.repository.PaymentRepository;
import com.payment.api.api.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired private ParentRepository parentRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private PaymentRepository paymentRepo;

    @Transactional
    public void processPayment(PaymentRequest request) {
        Parent parent = parentRepo.findById(request.parentId())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        Student student = studentRepo.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        double dynamicRate = 0.05; // Example dynamic fee or discount rate
        double adjustedAmount = request.paymentAmount() * (1 + dynamicRate);

        // Update student balance
        student.setBalance(student.getBalance() + adjustedAmount);
        studentRepo.save(student);

        if (student.getParents().size() == 2) { // Shared student
            for (Parent p : student.getParents()) {
                p.setBalance(p.getBalance() - adjustedAmount);
                parentRepo.save(p);
            }
        } else {
            parent.setBalance(parent.getBalance() - adjustedAmount);
            parentRepo.save(parent);
        }

        Payment payment = new Payment();
        payment.setParentId(parent.getId());
        payment.setStudentId(student.getId());
        payment.setAmount(adjustedAmount);
        payment.setTimestamp(LocalDateTime.now());
        paymentRepo.save(payment);
    }
}
