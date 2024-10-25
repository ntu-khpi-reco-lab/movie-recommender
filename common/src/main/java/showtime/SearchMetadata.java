package showtime;

import lombok.Data;

@Data
public class SearchMetadata {
    private String id;
    private String status;
    private String jsonEndpoint;
    private String createdAt;
    private String processedAt;
    private String googleUrl;
    private String rawHtmlFile;
    private double totalTimeTaken;
}