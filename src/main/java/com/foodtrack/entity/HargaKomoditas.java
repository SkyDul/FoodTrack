package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "harga_komoditas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class HargaKomoditas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_harga")
    private Integer idHarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_komoditas", nullable = false)
    private Komoditas komoditas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_satuan", nullable = false)
    private Satuan satuan;

    @NotNull(message = "Harga wajib diisi")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal harga;

    @Override
    public String toString() {
        return "HargaKomoditas{id=" + idHarga + ", harga=" + harga + "}";
    }
}
