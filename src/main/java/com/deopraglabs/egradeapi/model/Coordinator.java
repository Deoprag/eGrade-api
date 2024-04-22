package com.deopraglabs.egradeapi.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "coordinator")
public class Coordinator extends User {

        private static final long serialVersionUID = 1L;

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Transient
        private Role role = Role.COORDENADOR;

        @OneToMany(mappedBy = "coordinator", fetch = FetchType.LAZY)
        private List<Course> courses;
}
