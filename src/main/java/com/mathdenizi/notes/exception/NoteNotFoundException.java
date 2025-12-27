package com.mathdenizi.notes.exception;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
        super("Note not found or access denied");
    }
}
