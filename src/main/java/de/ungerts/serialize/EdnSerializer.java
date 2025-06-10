package de.ungerts.serialize;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;
import clojure.lang.PersistentVector;
import de.ungerts.model.BibEntry;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class EdnSerializer implements Serializer {


    @Override
    public String serialize(@NonNull BibEntry entry) {
        Map<Keyword, Object> clojureMap = convertToClojureMap(entry);
        IFn prStr = Clojure.var("clojure.core", "pr-str");
        Object result = prStr.invoke(clojureMap);
        if (result instanceof String edn) {
            return edn;
        } else {
            throw new IllegalStateException("Failed to serialize BibEntry to EDN");
        }
    }

    @Override
    public String serialize(@NonNull Iterable<BibEntry> entries) {
        List<Map<Keyword, Object>> clojureMaps =
                ((List<BibEntry>) entries).stream()
                        .map(this::convertToClojureMap)
                        .toList();
        PersistentVector entriesVector = PersistentVector.create(clojureMaps);
        IFn prStr = Clojure.var("clojure.core", "pr-str");
        Object result = prStr.invoke(entriesVector);
        if (result instanceof String edn) {
            return edn;
        } else {
            throw new IllegalStateException("Failed to serialize entries to EDN");
        }
    }

    private Map<Keyword, Object> convertToClojureMap(@NonNull BibEntry entry) {
        return Map.of(
                Keyword.intern("entry-type"), entry.entryType(),
                Keyword.intern("citation-key"), entry.citationKey(),
                Keyword.intern("title"), entry.title(),
                Keyword.intern("authors"), PersistentVector.create(entry.authors()),
                Keyword.intern("year"), entry.year(),
                Keyword.intern("journal"), entry.journal()
        );
    }

    @Override
    public void serialize(@NonNull Stream<BibEntry> stream, @NonNull Writer writer) {
        writeString(writer, "[\n");
        stream.forEach(entry -> {
            String entryString = serialize(entry) + "\n";
            writeString(writer, entryString);
        });
        writeString(writer, "]");
    }

    private static void writeString(Writer writer, String entryString) {
        try {
            writer.write(entryString);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
