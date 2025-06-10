package de.ungerts.serialize;

public class SerializerFactory {

    private SerializerFactory() {
        // Prevent instantiation
    }

    public static Serializer createSerializer(SerializerType type) {
        return switch (type) {
            case EDN -> new EdnSerializer();
            case JSON -> new JsonSerializer();
            case XML -> new XmlSerializer();
            case YAML -> new YamlSerializer();
        };
    }

}
