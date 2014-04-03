package ua.edu.sumdu.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonUnwrapped;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import ua.edu.sumdu.domain.FileTreeNode;
import ua.edu.sumdu.domain.Mp3File;

import java.io.IOException;

public class FileTreeNodeSerializer extends JsonSerializer<FileTreeNode> {

    @Override
    public void serialize(FileTreeNode node, JsonGenerator gen, SerializerProvider serializer) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        if (node.getBasePath().equals("/")) {
            gen.writeFieldName("attr");
            gen.writeStartObject();
            gen.writeFieldName("id");
            gen.writeString("root");
            gen.writeEndObject();
        }
        gen.writeFieldName("data");
        gen.writeString(node.getBasePath());
        gen.writeArrayFieldStart("children");

        ObjectMapper mapper = new ObjectMapper();
        for (FileTreeNode n : node.getNodes().values()) {
            mapper.writer().writeValue(gen, n);
        }

        for (Mp3File file : node.getChildren()) {
            mapper.writer().writeValue(gen, file);
        }

        gen.writeEndArray();
        gen.writeEndObject();
    }
}
