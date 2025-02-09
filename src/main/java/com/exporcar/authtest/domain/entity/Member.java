package com.exporcar.authtest.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_entity")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;



}
