package com.foodtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "distribusi")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Distribusi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distribusi")
    private Integer idDistribusi;

    @NotBlank(message = "Tujuan distribusi wajib diisi")
    @Column(nullable = false, length = 200)
    private String tujuan;

    @Builder.Default
    @Column(name = "status_pengiriman", length = 20)
    private String statusPengiriman = "Menunggu";

    @Column(name = "tanggal_kirim")
    private LocalDate tanggalKirim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stok", nullable = false)
    private StokPangan stokPangan;

    @Override
    public String toString() {
        return "Distribusi{id=" + idDistribusi + ", tujuan='" + tujuan + "'}";
    }
}
