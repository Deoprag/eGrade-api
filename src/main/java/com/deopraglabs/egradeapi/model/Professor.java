package com.deopraglabs.egradeapi.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "professor")
public class Professor extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Role role = Role.PROFESSOR;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "professor_course", // Change this to your desired join table name
            joinColumns = @JoinColumn(name = "professor_id"), // Column name in the join table referencing Professor
            inverseJoinColumns = @JoinColumn(name = "course_id") // Column name in the join table referencing Course
    )
    private List<Course> courses;
}
