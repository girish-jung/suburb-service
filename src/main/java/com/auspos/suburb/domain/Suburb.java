package com.auspos.suburb.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Entity
@Table(name = "suburb")
public class Suburb {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String suburbName;
    private long postCode;
    private String state;
    private String country;
}
