package de.ungerts.model;

import java.util.List;

public record BibEntry(
    String entryType,
    String citationKey,
    String title,
    List<String> authors,
    int year,
    String journal
) {}
