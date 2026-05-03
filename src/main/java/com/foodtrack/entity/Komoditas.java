package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
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

    /** Legacy field — kept for backward compatibility with StokPangan */
    @Column(nullable = true, length = 20)
    private String satuan;

    @OneToMany(mappedBy = "komoditas", cascade = CascadeType.ALL)
    private List<StokPangan> stokPanganList;

    @Builder.Default
    @OneToMany(mappedBy = "komoditas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Satuan> satuanList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "komoditas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HargaKomoditas> hargaList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "komoditas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KonversiSatuan> konversiList = new ArrayList<>();

    @Override
    public String toString() {
        return "Komoditas{id=" + idKomoditas + ", nama='" + namaKomoditas + "'}";
    }
}
