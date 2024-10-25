package showtime;

import lombok.Data;

@Data
public class SearchInformation {
    private String queryDisplayed;
    private int totalResults;
    private double timeTakenDisplayed;
    private String organicResultsState;
}