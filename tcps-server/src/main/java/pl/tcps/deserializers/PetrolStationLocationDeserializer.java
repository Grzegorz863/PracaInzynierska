package pl.tcps.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import pl.tcps.exceptions.WrongPetrolStationAddressException;
import pl.tcps.pojo.PetrolStationLocationCoordinates;

import java.io.IOException;

public class PetrolStationLocationDeserializer extends StdDeserializer<PetrolStationLocationCoordinates> {

    public PetrolStationLocationDeserializer(Class<?> vc) {
        super(vc);
    }

    public PetrolStationLocationDeserializer() {
        this(null);
    }

    @Override
    public PetrolStationLocationCoordinates deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {

        JsonNode googleApiResponseNode = p.getCodec().readTree(p);

        Double latitude;
        Double longitude;
        PetrolStationLocationCoordinates coordinates = null;

        if(googleApiResponseNode.get("status").textValue().equals("OK")) {
            JsonNode resultsNode = googleApiResponseNode.get("results");
            JsonNode firstResultNode = resultsNode.get(0);
            JsonNode locationNode = firstResultNode.get("geometry").get("location");
            latitude = locationNode.get("lat").doubleValue();
            longitude = locationNode.get("lng").doubleValue();
            coordinates = new PetrolStationLocationCoordinates(latitude, longitude);
            return coordinates;
        }
        else
            throw new JsonProcessingException("Address not found in Google API"){};
    }
}
