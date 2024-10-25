package showtime;

import lombok.Data;

import java.util.List;

@Data
public class MovieShowtimesResponse {

    private SearchMetadata searchMetadata;
    private SearchParameters searchParameters;
    private SearchInformation searchInformation;
    private List<Showtime> showtimes;
}