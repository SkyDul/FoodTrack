package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer idAdmin;

    @NotBlank(message = "Username wajib diisi")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Password wajib diisi")
    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String email;
}
