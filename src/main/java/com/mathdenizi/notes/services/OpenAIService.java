package com.mathdenizi.notes.services;

import com.mathdenizi.notes.models.examples.*;

public interface OpenAIService {

    GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest request);

    GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest);

    String getAnswer(String question);

    Answer getAnswer(Question question);
}