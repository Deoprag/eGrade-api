package com.deopraglabs.egradeapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "certificate")
public class Certificate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "justification", nullable = false, columnDefinition = "text")
    private String justification;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "image", nullable = false, columnDefinition = "text")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
