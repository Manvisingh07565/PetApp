package com.petapp.backend.service;

import com.petapp.backend.dto.SlotRequest;
import com.petapp.backend.entity.Slot;
import com.petapp.backend.entity.User;
import com.petapp.backend.repository.SlotRepository;
import com.petapp.backend.repository.UserRepository; // User repo chahiye hoga vet find karne ke liye
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    public Slot createSlot(SlotRequest request) {
        User vet = userRepository.findById(request.getVetId())
                .orElseThrow(() -> new RuntimeException("Vet not found"));

        Slot slot = new Slot();
        slot.setVet(vet);
        slot.setDate(request.getDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setConsultationType(request.getConsultationType());
        slot.setCapacity(request.getCapacity());
        slot.setBookedCount(0);

        return slotRepository.save(slot);
    }

    public List<Slot> getSlotsByVet(Long vetId) {
        return slotRepository.findByVetId(vetId);
    }
}