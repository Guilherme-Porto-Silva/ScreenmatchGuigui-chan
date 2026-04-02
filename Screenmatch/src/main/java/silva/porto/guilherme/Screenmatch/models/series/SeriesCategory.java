package silva.porto.guilherme.Screenmatch.models.series;

public enum SeriesCategory {

    ACTION("Action", "Ação"),

    DRAMA("Drama", "Drama"),

    ROMANCE("Romance", "Romance"),

    CRIME("Crime", "Crime"),

    COMEDY("Comedy", "Comédia");

    private final String EbertRichards;

    private final String OMBDJSON;

    SeriesCategory (String omdbjson, String ebertRichards) {

        OMBDJSON = omdbjson;

        EbertRichards = ebertRichards;
    }

    public static SeriesCategory parseSeriesCategory (String text) {

        for (SeriesCategory category: SeriesCategory.values()) {

            if (category.OMBDJSON.equalsIgnoreCase(text))

                return category;

            if (category.EbertRichards.equalsIgnoreCase(text))

                return category;
        }

        throw new IllegalArgumentException("No category match.");
    }
}