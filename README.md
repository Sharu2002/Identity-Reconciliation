Bitespeed Identify API

This Spring Boot application implements the `/identify` endpoint for Bitespeed to resolve and consolidate user identities based on email and phone number — even when users provide different contact information across orders.

Hosted Endpoint

**POST** `https://identity-reconciliation-37l0.onrender.com/identify`

Github Repository

`https://github.com/Sharu2002/Identity-Reconciliation.git`

Problem Statement

Doc Emmett Brown places orders using different emails and phone numbers to avoid detection.  
Bitespeed wants to unify his identity for a personalized experience on FluxKart.

Given a user's `email` and/or `phoneNumber`, the system should:
- Identify the primary contact
- Merge all linked contact information
- Create new secondary records if needed

Request Format

```json
POST /identify
Content-Type: application/json

{
  "email": "mcfly@hillvalley.edu",
  "phoneNumber": "123456"
}
```
Response Format

```json
{
  "contact": {
    "primaryContatctId": 1,
    "emails": ["lorraine@hillvalley.edu", "mcfly@hillvalley.edu"],
    "phoneNumbers": ["123456"],
    "secondaryContactIds": [23]
  }
}
```

Tech Stack

Java 21
Spring Boot 3
PostgreSQL 
JPA / Hibernate

Database Schema

CREATE TABLE contact (
  id SERIAL PRIMARY KEY,
  phone_number VARCHAR(15),
  email VARCHAR(255),
  linked_id INTEGER,
  link_precedence VARCHAR(10) CHECK (link_precedence IN ('PRIMARY', 'SECONDARY')),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

Logic Highlights

New contact → Created as PRIMARY if no match.
If either email or phone matches → existing contact is retrieved.
If a new combo appears → create SECONDARY linked to oldest primary.
Multiple PRIMARY contacts are resolved by choosing the oldest as final primary and demoting others.
