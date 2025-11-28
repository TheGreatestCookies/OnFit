package kspo.onfit.club.repository;

import static kspo.onfit.club.domain.QClub.club;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kspo.onfit.club.domain.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QClubRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Club> findClubByAreaAndSports(String area, String sports, Pageable pageable) {

        List<Club> content = jpaQueryFactory.select(club)
                .from(club)
                .where(areaEq(area), sportsEq(sports))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(club.count())
                .from(club)
                .where(areaEq(area), sportsEq(sports));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());

    }

    private BooleanExpression areaEq(String area) {
        return area == null ? null : club.area.eq(area);
    }

    private BooleanExpression sportsEq(String sports) {
        return sports == null ? null : club.sports.eq(sports);
    }

}