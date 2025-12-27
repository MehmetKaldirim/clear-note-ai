package com.mathdenizi.notes.services;

import com.mathdenizi.notes.models.AiResultResponse;
import com.mathdenizi.notes.models.Note;

import java.util.List;

public interface NoteService {

    Note createNoteForUser(String username, String content);

    Note updateNoteForUser(Long noteId, String content, String username);

    void deleteNoteForUser(Long noteId, String username);

    List<Note> getNotesForUser(String username);

    Note correctNoteWithAI(Long noteId, String username);

    List<String> getMistakes(Long noteId, String username);

    AiResultResponse getAiResult(Long noteId, String username);
}
