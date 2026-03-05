package com.responsys.data.service;

import com.responsys.data.dto.ContactRequest;
import com.responsys.data.dto.ContactResponse;
import com.responsys.data.dto.PagedResponse;
import com.responsys.data.entity.Contact;
import com.responsys.data.exception.ContactNotFoundException;
import com.responsys.data.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public PagedResponse<ContactResponse> getAll(int page, int size, String status) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Contact> result = StringUtils.hasText(status)
                ? contactRepository.findByStatus(status, pageable)
                : contactRepository.findAll(pageable);

        List<ContactResponse> content = result.getContent().stream()
                .map(this::toResponse).toList();

        return new PagedResponse<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    @Override
    public ContactResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public ContactResponse create(ContactRequest request) {
        Contact contact = toEntity(request);
        return toResponse(contactRepository.save(contact));
    }

    @Override
    @Transactional
    public List<ContactResponse> createBatch(List<ContactRequest> requests) {
        List<Contact> contacts = requests.stream().map(this::toEntity).toList();
        return contactRepository.saveAll(contacts).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = findOrThrow(id);
        contact.setEmail(request.getEmail());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhone());
        if (StringUtils.hasText(request.getStatus())) contact.setStatus(request.getStatus());
        if (StringUtils.hasText(request.getSourceFile())) contact.setSourceFile(request.getSourceFile());
        return toResponse(contactRepository.save(contact));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) throw new ContactNotFoundException(id);
        contactRepository.deleteById(id);
    }

    private Contact findOrThrow(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    private Contact toEntity(ContactRequest req) {
        return Contact.builder()
                .email(req.getEmail())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phone(req.getPhone())
                .status(StringUtils.hasText(req.getStatus()) ? req.getStatus() : "PENDING")
                .sourceFile(req.getSourceFile())
                .build();
    }

    private ContactResponse toResponse(Contact c) {
        return ContactResponse.builder()
                .id(c.getId())
                .email(c.getEmail())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .phone(c.getPhone())
                .status(c.getStatus())
                .sourceFile(c.getSourceFile())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
