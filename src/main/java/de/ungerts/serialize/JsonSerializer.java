package de.ungerts.serialize;

import de.ungerts.model.BibEntry;
import org.jspecify.annotations.NonNull;

import java.io.Writer;
import java.util.stream.Stream;

class JsonSerializer implements Serializer {
    @Override
    public String serialize(@NonNull BibEntry entry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String serialize(@NonNull Iterable<BibEntry> entries) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void serialize(@NonNull Stream<BibEntry> stream, @NonNull Writer writer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
