package ua.edu.sumdu.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import ua.edu.sumdu.domain.Mp3File;

import java.io.IOException;

public class Mp3FileSerializer extends JsonSerializer<Mp3File> {

    @Override
    public void serialize(Mp3File file, JsonGenerator gen, SerializerProvider serializer) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeFieldName("data");
        String filename = file.getFilename().substring(file.getFilename().lastIndexOf("/")+1);
        gen.writeString(filename);

        gen.writeFieldName("attr");

        gen.writeStartObject();
        gen.writeFieldName("fileId");
        gen.writeString(Integer.toString(file.getId()));
        gen.writeFieldName("duration");
        gen.writeString(Long.toString(file.getDuration()));
        gen.writeEndObject();

        gen.writeEndObject();
    }

}
