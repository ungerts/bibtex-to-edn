package de.ungerts;

import de.ungerts.serialize.Serializer;
import de.ungerts.serialize.SerializerFactory;
import de.ungerts.serialize.SerializerType;
import jakarta.ws.rs.InternalServerErrorException;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import de.ungerts.model.BibEntry;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/edn")
public class EdnResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdnResource.class);

    private final Serializer serializer = SerializerFactory.createSerializer(SerializerType.EDN);

    @GET
    @Produces("text/plain+edn")
    public String getEdn() {
        // Create sample BibEntry objects
        List<BibEntry> entries = createSampleEntries();
        try {
            String edn = serializer.serialize(entries);
            LOGGER.info("Successfully serialized entries to EDN format. EDN: {}", edn);
            return edn;
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Failed to serialize entries to EDN", e);
        }

    }

    @GET()
    @Path("/stream")
    @Produces("text/plain+edn")
    public Response getEdnStream() {
        // Create sample BibEntry objects
        List<BibEntry> entries = createSampleEntries();
        StreamingOutput output = outputStream -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            serializer.serialize(entries.stream(), writer);
            writer.flush();
        };
        return Response.ok(output).build();
    }

    private List<BibEntry> createSampleEntries() {
        List<BibEntry> entries = new ArrayList<>();

        entries.add(new BibEntry(
                "article",
                "smith2023deep",
                "Deep Learning in the Wild",
                List.of("John Smith", "Jane Doe"),
                2023,
                "Journal of AI Research"
        ));

        entries.add(new BibEntry(
                "book",
                "johnson2022machine",
                "Machine Learning: A Comprehensive Guide",
                List.of("Robert Johnson"),
                2022,
                "Tech Publishing"
        ));

        entries.add(new BibEntry(
                "inproceedings",
                "lee2023neural",
                "Neural Networks for Natural Language Processing",
                List.of("Sarah Lee", "Michael Chen", "David Kim"),
                2023,
                "Proceedings of International Conference on NLP"
        ));

        return entries;
    }

}