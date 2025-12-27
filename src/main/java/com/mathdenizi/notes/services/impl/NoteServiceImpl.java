package com.mathdenizi.notes.services.impl;

import com.mathdenizi.notes.exception.NoteNotFoundException;
import com.mathdenizi.notes.models.AiNoteCorrectionResponse;
import com.mathdenizi.notes.models.AiResultResponse;
import com.mathdenizi.notes.models.Note;
import com.mathdenizi.notes.services.AiNoteService;
import com.mathdenizi.notes.services.AuditLogService;
import com.mathdenizi.notes.services.NoteService;
import com.mathdenizi.notes.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AiNoteService aiNoteService;

    @Override
    public Note correctNoteWithAI(Long noteId, String username) {
        Note note = findUserNote(noteId, username);

        System.out.println("noteid = " + noteId);
        System.out.println("note kendisi = " + note.getContent());

        AiNoteCorrectionResponse aiResponse = aiNoteService.correctNote(note.getContent());

        // Basitleştirilmiş mapping: Mistake artık sadece string
        List<String> mistakes = aiResponse.mistakes();

        note.setCorrectedContent(aiResponse.correctedContent());
        note.setMistakes(mistakes);

        return noteRepository.save(note);
    }

    @Override
    public List<String> getMistakes(Long noteId, String username) {
        return findUserNote(noteId, username).getMistakes();
    }

    @Override
    public AiResultResponse getAiResult(Long noteId, String username) {
        Note note = findUserNote(noteId, username);
        return new AiResultResponse(
                note.getCorrectedContent(),
                note.getMistakes()
        );
    }

    // ---------------- HELPERS ----------------
    private Note findUserNote(Long noteId, String username) {
        return noteRepository.findByIdAndOwnerUsername(noteId, username)
                .orElseThrow(NoteNotFoundException::new);
    }

    @Override
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        Note savedNote = noteRepository.save(note);
        auditLogService.logNoteCreation(username, note);
        return savedNote;
    }

    @Override
    public Note updateNoteForUser(Long noteId, String content, String username) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new RuntimeException("Note not found"));
        note.setContent(content);
        Note updatedNote = noteRepository.save(note);
        auditLogService.logNoteUpdate(username, note);
        return updatedNote;
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new RuntimeException("Note not found"));
        auditLogService.logNoteDeletion(username, noteId);
        noteRepository.delete(note);
    }

    @Override
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findByOwnerUsername(username);
    }
}
