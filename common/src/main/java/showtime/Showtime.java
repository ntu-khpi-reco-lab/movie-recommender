package showtime;
import lombok.Data;
import java.util.List;

@Data
public class Showtime {
    private String day;
    private List<Theater> theaters;

    @Data
    public static class Theater {
        private String name;
        private String link;
        private String distance;
        private String address;
        private List<Showing> showing;
    }

    @Data
    public static class Showing {
        private List<String> time;
        private String type;
    }
}