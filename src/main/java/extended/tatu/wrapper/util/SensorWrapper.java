package extended.tatu.wrapper.util;

import extended.tatu.wrapper.model.Sensor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Uellington Damasceno
 */
public class SensorWrapper {

    private static final String INVALID_SENSOR = "INVALID_SENSOR";

    public static List<Sensor> getAllSensors(List sensors) {
        return SensorWrapper.getAllSensors(new JSONArray(sensors));
    }

    public static List<Sensor> getAllSensors(JSONArray sensors) {
        return (sensors == null || sensors.isEmpty())
                ? new ArrayList()
                : sensors.toList()
                        .stream()
                        .filter(Map.class::isInstance)
                        .map(Map.class::cast)
                        .map(SensorWrapper::toSensor)
                        .collect(Collectors.toList());
    }

    public static List<String> getAllJSONSensor(List<Sensor> sensor) {
        return getAllJSONObjectSensor(sensor).stream()
                .map(JSONObject::toString)
                .collect(Collectors.toList());
    }

    public static List<JSONObject> getAllJSONObjectSensor(List<Sensor> sensors) {
        return (sensors == null || sensors.isEmpty())
                ? new ArrayList()
                : sensors.stream()
                        .map(SensorWrapper::toJSONObject)
                        .collect(Collectors.toList());
    }

    public static Sensor toSensor(Map sensor) {
        String id = (String) sensor.getOrDefault("id", INVALID_SENSOR);
        String type = (String) sensor.getOrDefault("type", INVALID_SENSOR);
        int collectionTime = (int) sensor.getOrDefault("collection_time", 0);
        int publishingTime = (int) sensor.getOrDefault("publishing_time", 0);
        
        int minValue = (int) sensor.getOrDefault("min_value", Sensor.DEFAULT_MIN_VALUE);
        int maxValue = (int) sensor.getOrDefault("max_value", Sensor.DEFAULT_MAX_VALUE);
        int delta = (int) sensor.getOrDefault("delta", Sensor.DEFAULT_DELTA);
        
        return new Sensor(id, type, collectionTime, publishingTime, minValue, maxValue, delta);
    }

    public static Sensor toSensor(String sensor) {
        return toSensor(new JSONObject(sensor));
    }

    public static Sensor toSensor(JSONObject sensor) {
        String id = sensor.getString("id");
        String type = sensor.getString("type");
        int collectionTime = sensor.getInt("collection_time");
        int publishingTime = sensor.getInt("publishing_time");
        
        int minValue = sensor.has("min_value") ? sensor.getInt("min_value") : Sensor.DEFAULT_MIN_VALUE;
        int maxValue = sensor.has("max_value") ? sensor.getInt("max_value") : Sensor.DEFAULT_MAX_VALUE;
        int delta = sensor.has("delta") ? sensor.getInt("delta") : Sensor.DEFAULT_DELTA;
        
        return new Sensor(id, type, collectionTime, publishingTime, minValue, maxValue, delta);
    }

    public static String toJSON(Sensor sensor) {
        return toJSONObject(sensor).toString();
    }

    public static JSONObject toJSONObject(Sensor sensor) {
        return new JSONObject()
                .put("id", sensor.getId())
                .put("type", sensor.getType())
                .put("collection_time", sensor.getCollectionTime())
                .put("publishing_time", sensor.getPublishingTime());
    }
}
