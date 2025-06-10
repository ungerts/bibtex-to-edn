package de.ungerts.serialize;

public enum SerializerType {

    EDN("EDN"), JSON("JSON"), XML("XML"), YAML("YAML");

    private final String name;

    SerializerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
