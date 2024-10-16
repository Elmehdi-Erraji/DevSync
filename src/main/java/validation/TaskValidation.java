package validation;

import java.time.LocalDate;

public class TaskValidation {

    public static boolean isValidStartDate(LocalDate startDate) {
        LocalDate today = LocalDate.now();
        return !startDate.isBefore(today.plusDays(3));
    }

    public static boolean isValidDueDate(LocalDate startDate, LocalDate dueDate) {
        return !dueDate.isBefore(startDate.plusDays(1));
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 100; // Example length check
    }

    public static boolean isValidDescription(String description) {
        return description != null && !description.trim().isEmpty() && description.length() <= 500; // Example length check
    }

    public static boolean isValidUser(Long userId) {
        return userId != null && userId > 0;
    }


    public static boolean isValidTags(String[] tagIds) {
        return tagIds != null && tagIds.length > 0;
    }

    public static boolean isValid(String title, String description, Long creatorId, Long assignedUserId,
                                  String[] tagIds, LocalDate startDate, LocalDate dueDate) {
        if (isNullOrEmpty(title) || isNullOrEmpty(description) || creatorId == null || assignedUserId == null) {
            return false;
        }

        if (startDate != null && dueDate != null && startDate.isAfter(dueDate)) {
            return false;
        }

        if (tagIds != null && tagIds.length == 0) {
            return false;
        }

        // If everything is valid, return true
        return true;
    }

    // Helper method to check for null or empty strings
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
