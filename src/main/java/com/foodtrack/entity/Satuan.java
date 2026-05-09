package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "satuan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Satuan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_satuan")
    private Integer idSatuan;

    @NotBlank(message = "Nama satuan wajib diisi")
    @Column(name = "nama_satuan", nullable = false, length = 50)
    private String namaSatuan;

    @Column(name = "simbol", length = 20)
    private String simbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_komoditas", nullable = false)
    private Komoditas komoditas;

    @Builder.Default
    @Column(name = "is_base_unit")
    private Boolean isBaseUnit = false;

    @Column(name = "konversi_ke_base", precision = 15, scale = 4)
    private java.math.BigDecimal konversiKeBase;

    @Transient
    private java.math.BigDecimal konversiNilai;

    @Transient
    private java.math.BigDecimal harga;

    @Override
    public String toString() {
        return "Satuan{id=" + idSatuan + ", nama='" + namaSatuan + "'}";
    }
}
