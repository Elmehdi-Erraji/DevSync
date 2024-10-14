package service;

import domain.Request;
import domain.enums.RequestStatus;
import exception.RequestException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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

    public Request saveRequest(Request request) {
        validateRequest(request);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    public Request rejectRequest(Long requestId) {
        Request request = findRequestByIdOrThrow(requestId);
        if (request.getStatus() == RequestStatus.REJECTED) {
            throw new RequestException("Request is already rejected.");
        }
        request.setStatus(RequestStatus.REJECTED); // Set status to REJECTED
        return requestRepository.save(request);
    }

    public Optional<Request> getRequestById(Long id) {
        if (id == null) {
            throw new RequestException("Request ID cannot be null.");
        }
        return requestRepository.findById(id);
    }

    public List<Request> getAllRequests() {

        return requestRepository.findAll();
    }

    public void deleteRequest(Long requestId) {
        findRequestByIdOrThrow(requestId);
        requestRepository.delete(requestId);
    }

    public Request updateRequestStatus(Long requestId, RequestStatus newStatus) {
        if (newStatus == null) {
            throw new RequestException("New status cannot be null.");
        }
        Request request = findRequestByIdOrThrow(requestId);
        if (request.getStatus() == newStatus) {
            throw new RequestException("Request already has status: " + newStatus);
        }
        request.setStatus(newStatus);
        return requestRepository.save(request);
    }

    private void validateRequest(Request request) {
        if (request == null) {
            throw new RequestException("Request cannot be null.");
        }
        if (request.getRequestType() == null) {
            throw new RequestException("Request type cannot be null.");
        }
        if (request.getTask() == null) {
            throw new RequestException("Task cannot be null.");
        }
        if (request.getUser() == null) {
            throw new RequestException("User cannot be null.");
        }
    }

    private Request findRequestByIdOrThrow(Long requestId) {
        if (requestId == null) {
            throw new RequestException("Request ID cannot be null.");
        }
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestException("Request not found with ID: " + requestId));
    }
}
