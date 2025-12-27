package com.mathdenizi.notes.models;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;



public record AiNoteCorrectionResponse(

        @JsonPropertyDescription("The fully corrected version of the note")
        String correctedContent,

        @JsonPropertyDescription("List of detected mistakes in the note as plain strings")
        List<String> mistakes
) {}
