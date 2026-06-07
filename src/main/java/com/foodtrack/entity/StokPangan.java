package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "stok_pangan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)


/**
 * Entitas atau model data untuk StokPangan.
 * Digunakan untuk merepresentasikan struktur tabel di database.
 */
public class StokPangan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stok")
    private Integer idStok;

    @NotNull(message = "Jumlah masuk wajib diisi")
    @Min(value = 1, message = "Jumlah minimal 1")
    @Column(name = "jumlah_masuk", nullable = false)
    private Integer jumlahMasuk;

    @NotNull(message = "Tanggal panen wajib diisi")
    @Column(name = "tanggal_panen", nullable = false)
    private LocalDate tanggalPanen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petani", nullable = false)
    private Petani petani;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_komoditas", nullable = false)
    private Komoditas komoditas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_satuan")
    private Satuan satuan;

    @OneToMany(mappedBy = "stokPangan", cascade = CascadeType.ALL)
    private List<Distribusi> distribusiList;

    @Override
    public String toString() {
        return "StokPangan{id=" + idStok + ", jumlah=" + jumlahMasuk + "}";
    }
}

