package kspo.onfit.voucher.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kspo.onfit.voucher.domain.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QVoucherRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Voucher> findVoucherByAreaAndSports(String area, String sports, Pageable pageable){

        List<Voucher> content = jpaQueryFactory.select(voucher)
                .from(voucher)
                .where(areaEq(area), sportsEq(sports))
                .orderBy(voucher.telephone.desc(), voucher.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(voucher.count())
                .from(voucher)
                .where(areaEq(area), sportsEq(sports));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    private BooleanExpression areaEq(String area){
        return area == null ? null : voucher.area.eq(area);
    }

    private BooleanExpression sportsEq(String sports){
        return sports == null ? null : voucher.sports.eq(sports);
    }

}
