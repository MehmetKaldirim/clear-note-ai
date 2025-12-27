package com.mathdenizi.notes.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User input (never modified)
    @Lob
    private String content;

    // AI corrected version
    @Lob
    private String correctedContent;

    private String ownerUsername;

    private LocalDateTime createdAt;

    // AI detected mistakes
    @ElementCollection
    @CollectionTable(name = "note_mistakes", joinColumns = @JoinColumn(name = "note_id"))
    private List<String> mistakes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.correctedContent == null) {
            this.correctedContent = "This note has not been corrected by AI yet.";
        }
    }
}
