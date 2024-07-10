package com.deopraglabs.egradeapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "student")
public class Student extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Transient
    private Role role = Role.ALUNO;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Grade> grades;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Attendance> attendances;

}
