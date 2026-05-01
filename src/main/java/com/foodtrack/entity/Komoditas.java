package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "komoditas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Komoditas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_komoditas")
    private Integer idKomoditas;

    @NotBlank(message = "Nama komoditas wajib diisi")
    @Column(name = "nama_komoditas", nullable = false, length = 100)
    private String namaKomoditas;

    @NotBlank(message = "Satuan wajib diisi")
    @Column(nullable = false, length = 20)
    private String satuan;

    @OneToMany(mappedBy = "komoditas", cascade = CascadeType.ALL)
    private List<StokPangan> stokPanganList;

    @Override
    public String toString() {
        return "Komoditas{id=" + idKomoditas + ", nama='" + namaKomoditas + "'}";
    }
}
