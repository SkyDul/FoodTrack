package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)


/**
 * Entitas atau model data untuk Admin.
 * Digunakan untuk merepresentasikan struktur tabel di database.
 */
// [OOP: Inheritance / Pewarisan]
// Keyword 'extends' membuktikan kelas Admin adalah class anak (Sub-class) 
// yang mewarisi sifat dari class induk (Super-class) BaseEntity.
public class Admin extends BaseEntity {

    // [OOP: Encapsulation / Enkapsulasi]
    // modifier 'private' mencegah properti idAdmin diakses bebas dari luar. 
    // Harus lewat fungsi (method) Getter/Setter.
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

