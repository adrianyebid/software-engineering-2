package com.gymapp.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    public void activate() {
        if (Boolean.TRUE.equals(this.isActive)) return;
        this.isActive = true;
    }

    public void deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) return;
        this.isActive = false;
    }
}