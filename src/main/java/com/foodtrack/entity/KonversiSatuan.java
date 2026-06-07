package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "konversi_satuan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)


/**
 * Entitas atau model data untuk KonversiSatuan.
 * Digunakan untuk merepresentasikan struktur tabel di database.
 */
public class KonversiSatuan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_konversi")
    private Integer idKonversi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_satuan_dari", nullable = false)
    private Satuan satuanDari;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_satuan_ke", nullable = false)
    private Satuan satuanKe;

    @NotNull(message = "Nilai konversi wajib diisi")
    @Column(name = "nilai_konversi", nullable = false, precision = 15, scale = 4)
    private BigDecimal nilaiKonversi;

    @Column(name = "konversi_ke_base", precision = 15, scale = 4)
    private BigDecimal konversiKeBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_komoditas", nullable = false)
    private Komoditas komoditas;

    @Override
    public String toString() {
        return "KonversiSatuan{id=" + idKonversi + ", nilai=" + nilaiKonversi + "}";
    }
}

