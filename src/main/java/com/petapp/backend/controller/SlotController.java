package com.petapp.backend.controller;

import com.petapp.backend.dto.SlotRequest;
import com.petapp.backend.entity.Slot;
import com.petapp.backend.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173") // React Frontend URL (Adjust if needed)
public class SlotController {

    @Autowired
    private SlotService slotService;

    // POST http://localhost:8080/api/slots/create
    @PostMapping("/create")
    public ResponseEntity<Slot> createSlot(@RequestBody SlotRequest request) {
        Slot createdSlot = slotService.createSlot(request);
        return ResponseEntity.ok(createdSlot);
    }

    // GET http://localhost:8080/api/slots/vet/{vetId}
    @GetMapping("/vet/{vetId}")
    public ResponseEntity<List<Slot>> getVetSlots(@PathVariable Long vetId) {
        return ResponseEntity.ok(slotService.getSlotsByVet(vetId));
    }
}