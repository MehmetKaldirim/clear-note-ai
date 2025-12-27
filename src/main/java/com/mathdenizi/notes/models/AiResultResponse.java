package com.mathdenizi.notes.models;

import java.util.List;

public record AiResultResponse(
        String correctedContent,
        List<String> mistakes
) {}
