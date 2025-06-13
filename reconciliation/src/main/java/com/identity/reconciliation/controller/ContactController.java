package com.identity.reconciliation.controller;

import com.identity.reconciliation.dto.ContactRequestDto;
import com.identity.reconciliation.dto.ContactResponseDto;
import com.identity.reconciliation.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
@RequiredArgsConstructor
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponseDto> identify(@RequestBody ContactRequestDto request) {
        return ResponseEntity.ok(contactService.identify(request));
    }
}