package silva.porto.guilherme.Screenmatch.service.json;

public interface IConvertData {

    public <T> T convertData (String json, Class<T> type);
}