package com.prophius.sma.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @Column(name="time_created")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="time_updated")
    @CreationTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne()
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name="post_id", referencedColumnName = "post_id", nullable = false)
    private Post post;

}
