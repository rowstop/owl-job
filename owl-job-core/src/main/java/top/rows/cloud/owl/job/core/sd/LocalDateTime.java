package top.rows.cloud.owl.job.core.sd;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author 张治保
 * @since 2024/7/31
 */
public class LocalDateTime {

    public static final Se SERIALIZER = new Se();
    public static final De DESERIALIZER = new De();

    private static class Se extends JsonSerializer<java.time.LocalDateTime> {
        @Override
        public void serialize(java.time.LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            gen.writeString(value.toString());
        }

    }

    private static class De extends JsonDeserializer<java.time.LocalDateTime> {

        @Override
        public java.time.LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return java.time.LocalDateTime.parse(p.getValueAsString());
        }
    }


}
