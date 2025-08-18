package com.mathdenizi.notes.services;


import com.mathdenizi.notes.models.Note;

import java.util.List;
/**
 * Created by mathdenizi
 * Date: 27.06.25
 */
public interface NoteService {
    Note createNoteForUser(String username, String content);

    Note updateNoteForUser(Long noteId, String content, String username);

    void deleteNoteForUser(Long noteId, String username);

    List<Note> getNotesForUser(String username);
}
