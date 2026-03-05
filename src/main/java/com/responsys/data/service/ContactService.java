package com.responsys.data.service;

import com.responsys.data.dto.ContactRequest;
import com.responsys.data.dto.ContactResponse;
import com.responsys.data.dto.PagedResponse;

import java.util.List;

public interface ContactService {
    PagedResponse<ContactResponse> getAll(int page, int size, String status);
    ContactResponse getById(Long id);
    ContactResponse create(ContactRequest request);
    List<ContactResponse> createBatch(List<ContactRequest> requests);
    ContactResponse update(Long id, ContactRequest request);
    void delete(Long id);
}
