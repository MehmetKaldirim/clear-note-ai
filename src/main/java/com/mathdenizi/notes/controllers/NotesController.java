package com.mathdenizi.notes.controllers;


import com.mathdenizi.notes.models.AiNoteCorrectionResponse;
import com.mathdenizi.notes.models.AiResultResponse;
import com.mathdenizi.notes.models.Note;
import com.mathdenizi.notes.services.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Created by mathdenizi
 * Date: 27.06.25
 */


@RestController
@RequestMapping("/api/notes")
public class NotesController {

    private final NoteService noteService;


    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ---------------- BASIC CRUD ----------------

    @PostMapping
    public Note createNote(@RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.createNoteForUser(userDetails.getUsername(), content);
    }

    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        return noteService.getNotesForUser(userDetails.getUsername());
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.updateNoteForUser(noteId, content, userDetails.getUsername());
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        noteService.deleteNoteForUser(noteId, userDetails.getUsername());
    }

    // ---------------- AI ENDPOINTS ----------------

    /**
     * AI ile notu düzeltir (grammar, spelling, style)
     */
    @PostMapping("/{noteId}/ai/correct")
    public Note correctNoteWithAI(@PathVariable Long noteId,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("note controller id :" + noteId);
        return noteService.correctNoteWithAI(noteId, userDetails.getUsername());


    }

    /**
     * Sadece AI hatalarını döner
     */
    @GetMapping("/{noteId}/ai/mistakes")
    public List<String> getNoteMistakes(@PathVariable Long noteId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.getMistakes(noteId, userDetails.getUsername());
    }

    /**
     * AI sonucu (correctedContent + mistakes)
     */
    @GetMapping("/{noteId}/ai/result")
    public AiResultResponse getAiResult(@PathVariable Long noteId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.getAiResult(noteId, userDetails.getUsername());
    }
}

