package com.apps.historyservice.controller;

import com.apps.historyservice.entity.QuantityMeasurementEntity;
import com.apps.historyservice.entity.UserEntity;
import com.apps.historyservice.repository.QuantityMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
public class HistoryController {

    @Autowired
    private QuantityMeasurementRepository repository;

    private UserEntity getCurrentUser() {
        if(SecurityContextHolder.getContext().getAuthentication() == null) return null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email == null || email.equals("anonymousUser")) return null;
        UserEntity user = new UserEntity();
        user.setEmail(email);
        return user;
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuantityMeasurementEntity>> getHistory() {
        UserEntity user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Collections.emptyList());
        return ResponseEntity.ok(repository.findByUser(user));
    }

    @GetMapping("/history/{operation}")
    public ResponseEntity<List<QuantityMeasurementEntity>> getHistoryByOperation(@PathVariable String operation) {
        UserEntity user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Collections.emptyList());
        return ResponseEntity.ok(repository.findByUserAndOperation(user, operation.toUpperCase()));
    }

    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        UserEntity user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(0L);
        return ResponseEntity.ok(repository.countByUserAndOperationAndErrorFalse(user, operation.toUpperCase()));
    }

    // INTERNAL API for the Conversion Service to save an operation
    @PostMapping("/internal/save")
    public ResponseEntity<QuantityMeasurementEntity> saveMeasurement(@RequestBody QuantityMeasurementEntity entity) {
        entity.setUser(getCurrentUser());
        return ResponseEntity.ok(repository.save(entity));
    }
}
