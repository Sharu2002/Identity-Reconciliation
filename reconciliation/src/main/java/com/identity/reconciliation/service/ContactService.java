package com.identity.reconciliation.service;

import com.identity.reconciliation.dto.ContactRequestDto;
import com.identity.reconciliation.dto.ContactResponseDto;
import com.identity.reconciliation.entity.ContactEntity;
import com.identity.reconciliation.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    public ContactResponseDto identify(ContactRequestDto request) {

        String email = request.getEmail();
        String phone = request.getPhoneNumber();
        LocalDateTime now = LocalDateTime.now();

        List<ContactEntity> matches = contactRepository.findByEmailOrPhoneNumber(email, phone);

        Set<ContactEntity> allLinkedContacts = new HashSet<>();

        for (ContactEntity c : matches) {
            //means that the contact is primary
            if (c.getLinkedId() == null) {
                //adding primary to the list
                allLinkedContacts.add(c);
                //adding all the secondary contacts linked to the primary contact
                allLinkedContacts.addAll(contactRepository.findByEmailOrPhoneNumber(c.getEmail(), c.getPhoneNumber()));
            }
            //means that the contact is secondary
            else {
                //fetch the primary contact and add it to the list
                Optional<ContactEntity> primary = contactRepository.findById(c.getLinkedId());
                primary.ifPresent(allLinkedContacts::add);
                //add the secondary contact to the list
                allLinkedContacts.add(c);
            }
        }

        List<ContactEntity> allContacts = new ArrayList<>(allLinkedContacts);

        //no contacts found
        if (allContacts.isEmpty()) {
            // Create new primary contact
            ContactEntity newContact = new ContactEntity(null, phone, email, null, ContactEntity.LinkPrecedence.PRIMARY, now, now, null);

            //save to the db
            newContact = contactRepository.save(newContact);
            return new ContactResponseDto(
                    new ContactResponseDto.ContactDTO(newContact.getId(),
                            List.of(email),
                            List.of(phone),
                            List.of())
            );
        }

        //if contacts found, determine the primary contact
        ContactEntity primary = allContacts.stream()
                .filter(c -> c.getLinkPrecedence() == ContactEntity.LinkPrecedence.PRIMARY)
                .min(Comparator.comparing(ContactEntity::getCreatedAt))
                .orElseThrow();

        Set<String> emails = new LinkedHashSet<>();
        Set<String> phones = new LinkedHashSet<>();
        List<Integer> secondaryIds = new ArrayList<>();

        //create the response body
        for (ContactEntity c : allContacts) {

            if (c.getEmail() != null){
                emails.add(c.getEmail());
            }
            if (c.getPhoneNumber() != null){
                phones.add(c.getPhoneNumber());
            }

            if (!c.getId().equals(primary.getId())) {
                secondaryIds.add(c.getId());

                if (c.getLinkPrecedence() == ContactEntity.LinkPrecedence.PRIMARY) {
                    c.setLinkPrecedence(ContactEntity.LinkPrecedence.SECONDARY);
                    c.setLinkedId(primary.getId());
                    c.setUpdatedAt(now);
                    contactRepository.save(c);
                }
            }
        }

        // checking if a contact with the email and number exists
        boolean alreadyExists = allContacts.stream().anyMatch(c ->
                Objects.equals(c.getEmail(), email) &&
                        Objects.equals(c.getPhoneNumber(), phone)
        );

        //create a secondary contact if it does not already exist
        if (!alreadyExists) {
            ContactEntity secondary = new ContactEntity(null, phone, email, primary.getId(), ContactEntity.LinkPrecedence.SECONDARY, now, now, null);
            contactRepository.save(secondary);
            secondaryIds.add(secondary.getId());
            if (email != null) emails.add(email);
            if (phone != null) phones.add(phone);
        }

        return new ContactResponseDto(
                new ContactResponseDto.ContactDTO(primary.getId(),
                        new ArrayList<>(emails),
                        new ArrayList<>(phones),
                        secondaryIds)
        );

    }
}
