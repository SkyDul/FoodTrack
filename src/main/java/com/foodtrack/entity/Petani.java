package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "petani")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Petani extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_petani")
    private Integer idPetani;

    @NotBlank(message = "Nama petani wajib diisi")
    @Column(nullable = false, length = 100)
    private String nama;

    @Column(name = "kelompok_tani", length = 100)
    private String kelompokTani;

    @NotBlank(message = "Kontak wajib diisi")
    @Column(nullable = false, length = 20)
    private String kontak;

    // BONUS: untuk login petani
    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 255)
    private String password;

    @OneToMany(mappedBy = "petani", cascade = CascadeType.ALL)
    private List<StokPangan> stokPanganList;

    @OneToMany(mappedBy = "petani", cascade = CascadeType.ALL)
    private List<ChatbotLog> chatbotLogList;

    @Override
    public String toString() {
        return "Petani{id=" + idPetani + ", nama='" + nama + "'}";
    }
}
