package club.starcard.modules.member.specification;

import club.starcard.modules.member.entity.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public class CrmSpecification {

    public static Specification<Member> queryMemberSpecification(final Date beginDate, final Date endDate, final String search) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                Path<Date> createTime = root.get("createTime");
                Path<String> idCard = root.get("idCard");
                Path<String> name = root.get("name");
                Predicate predicate = null;
                if (StringUtils.isNotBlank(search)) {
                    predicate = builder.and(builder.or(builder.like(name, search), builder.like(idCard, search)));
                }
                if (beginDate != null) {
                    if (predicate != null) {
                        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(createTime, beginDate));
                    } else {
                        predicate = builder.and(builder.greaterThanOrEqualTo(createTime, beginDate));
                    }
                }
                if (endDate != null) {
                    if (predicate != null) {
                        predicate = builder.and(predicate, builder.lessThanOrEqualTo(createTime, endDate));
                    } else {
                        predicate = builder.and(builder.greaterThanOrEqualTo(createTime, beginDate));
                    }
                }
                return predicate;
            }
        };
    }


}
