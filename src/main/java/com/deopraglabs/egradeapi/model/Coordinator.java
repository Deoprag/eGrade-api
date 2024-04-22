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

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Transient
        private Role role = Role.COORDENADOR;

        @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Course> courses;
}
