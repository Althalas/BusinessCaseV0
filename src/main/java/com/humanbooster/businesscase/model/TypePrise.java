package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "TypePrise", uniqueConstraints = @UniqueConstraint(columnNames = "NomTypePrise"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypePrise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypePrise;

    @Column(name = "NomTypePrise", nullable = false, length = 50)
    private String nomTypePrise; // Ex: "Type 2S"

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;
}
