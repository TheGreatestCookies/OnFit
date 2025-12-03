package kspo.onfit.voucher.repository;

import kspo.onfit.voucher.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    /**
     * 주어진 좌표에서 가까운 순으로 50개의 바우처 조회 (거리 포함)
     */
    @Query(value = """
            SELECT v.id, v.name, v.facility_name, v.addr1, v.sports, v.price, v.telephone,
                   ROUND(ST_Distance_Sphere(
                       v.location,
                       ST_SRID(POINT(:lng, :lat), 4326)
                   ) / 1000, 1) AS distance
            FROM voucher v
            WHERE v.location IS NOT NULL
            ORDER BY distance ASC
            LIMIT 50
            """, nativeQuery = true)
    List<Object[]> findNearestVouchersWithDistance(@Param("lat") double lat, @Param("lng") double lng);
    @Override
    Voucher getReferenceById(Long id);

}
