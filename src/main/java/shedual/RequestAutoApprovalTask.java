package shedual;

import domain.Request;
import domain.enums.RequestStatus;
import domain.User;
import service.RequestService;
import service.UserService;
import java.util.List;
import java.util.TimerTask;
import java.time.LocalDateTime;

public class RequestAutoApprovalTask extends TimerTask {

    private final RequestService requestService;
    private final UserService userService;

    public RequestAutoApprovalTask(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @Override
    public void run() {
        List<Request> pendingRequests = requestService.findPendingRequests();
        LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);

        for (Request request : pendingRequests) {
            if (request.getRequestDate().isBefore(twelveHoursAgo)) {
                request.setStatus(RequestStatus.APPROVED);

                requestService.updateRequest(request);

                User user = request.getUser();
                if (user != null) {
                    int currentDailyTokens = user.getDailyTokens();
                    int currentMonthlyTokens = user.getMonthlyTokens();

                    userService.updateUserTokens((long) user.getId(), currentDailyTokens + 4, currentMonthlyTokens);
                }
            }
        }
    }
}
