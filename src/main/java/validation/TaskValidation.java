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
}
