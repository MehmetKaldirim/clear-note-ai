package com.mathdenizi.notes.models;


import jakarta.persistence.*;
import lombok.Data;
/**
 * Created by mathdenizi
 * Date: 27.06.25
 */
@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private String ownerUsername;
}
