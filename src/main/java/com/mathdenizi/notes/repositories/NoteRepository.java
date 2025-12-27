package com.mathdenizi.notes.repositories;

import com.mathdenizi.notes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwnerUsername(String ownerUsername);

    Optional<Note> findByIdAndOwnerUsername(Long id, String ownerUsername);

    List<Note> findAllByOwnerUsernameOrderByCreatedAtDesc(String ownerUsername);
}