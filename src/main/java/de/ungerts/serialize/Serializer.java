package de.ungerts.serialize;

import de.ungerts.model.BibEntry;
import org.jspecify.annotations.NonNull;

import java.io.Writer;
import java.util.stream.Stream;

public interface Serializer {

    String serialize(@NonNull BibEntry entry);

    String serialize(@NonNull Iterable<BibEntry> entries);

    void serialize(@NonNull Stream<BibEntry> stream, @NonNull Writer writer);
}
