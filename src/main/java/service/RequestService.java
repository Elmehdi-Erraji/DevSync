package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import domain.Request;
import domain.enums.RequestStatus;
import repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RequestService {

    private final RequestRepository requestRepository;

    public RequestService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.requestRepository = new RequestRepository(entityManager);
    }

    // Save a new request
    public Request saveRequest(Request request) {
        request.setRequestDate(LocalDateTime.now());  // Set current date and time
        request.setStatus(RequestStatus.PENDING);     // Default status is PENDING
        return requestRepository.save(request);
    }

    // Reject an existing request
    public Request rejectRequest(Long requestId) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            request.setStatus(RequestStatus.REJECTED);  // Change status to REJECTED
            return requestRepository.save(request);     // Save the updated request
        } else {
            throw new RuntimeException("Request not found with id: " + requestId);
        }
    }

    // Retrieve a request by ID
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
}
