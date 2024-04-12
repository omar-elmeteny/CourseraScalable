package profilemanagement.utils;

import java.util.List;

public class PaginationUtils {

    /**
     * Returns a sublist of the content list based on the specified page and size.
     *
     * @param content the original content list
     * @param page the page number (1-based index)
     * @param size the number of items per page
     * @return a sublist representing the content for the specified page and size
     */
    public static <T> List<T> slicedContent(List<T> content, int page, int size) {
        int totalItems = content.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Validate page number
        if (page < 1 || page > totalPages) {
            throw new IllegalArgumentException("Invalid page number");
        }

        int startIndex = (page - 1) * size; // Calculate start index for the page
        int endIndex = Math.min(startIndex + size, totalItems); // Calculate end index

        if (startIndex >= totalItems) {
            return List.of(); // Return an empty list if start index is out of bounds
        }

        return content.subList(startIndex, endIndex);
    }


}
