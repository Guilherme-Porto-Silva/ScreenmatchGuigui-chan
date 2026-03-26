package silva.porto.guilherme.Screenmatch.models.series;

public enum SeriesCategory {

    ACTION("Action"),

    DRAMA("Drama"),

    ROMANCE("Romance"),

    CRIME("Crime"),

    COMEDY("Comedy");

    private final String OMBDJSON;

    SeriesCategory (String omdbjson) { OMBDJSON = omdbjson; }

    public static SeriesCategory parseSeriesCategory (String text) {

        for (SeriesCategory category: SeriesCategory.values())

            if (category.OMBDJSON.equalsIgnoreCase(text))

                return category;

        throw new IllegalArgumentException("No category match.");
    }
}