package movie;

import lombok.Data;
import java.util.List;

@Data
public class MovieCredits {
    private int id;
    private List<CastMember> cast;

    @Data
    public static class CastMember {
        private boolean adult;
        private int gender;
        private int id;
        private String knownForDepartment;
        private String name;
        private String originalName;
        private double popularity;
        private String profilePath;
        private int castId;
        private String character;
        private String creditId;
        private int order;
    }
}