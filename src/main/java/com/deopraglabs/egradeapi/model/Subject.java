package com.deopraglabs.egradeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "subject")
public class Subject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private List<Grade> grades;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private List<SubjectCourse> subjectCourse;
}
