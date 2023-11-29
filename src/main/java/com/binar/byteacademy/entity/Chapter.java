package com.binar.byteacademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chapter_id")
    private UUID id;

    @Column(name = "no_chapter", nullable = false)
    private Integer noChapter;

    @Column(nullable = false)
    private String title;

    @Column(name = "chapter_duration")
    private Integer chapterDuration;

<<<<<<< dev_akbar
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
=======
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
>>>>>>> main
    private List<Material> materials;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;
}
