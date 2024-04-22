package com.deopraglabs.egradeapi.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "student")
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Role role = Role.ALUNO;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
