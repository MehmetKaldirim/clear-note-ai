package com.mathdenizi.notes.services;


import com.mathdenizi.notes.models.AiNoteCorrectionResponse;

public interface AiNoteService {

    AiNoteCorrectionResponse correctNote(String content);
}
