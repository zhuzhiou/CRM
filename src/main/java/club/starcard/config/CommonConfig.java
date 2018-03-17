package club.starcard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by wlrllr on 2018/3/14.
 */
@ConfigurationProperties(prefix = "crm")
@Configuration
@Data
public class CommonConfig {

    private Map<String,Integer[]> rankStrategy;
    /**
     * 分组策略
     * 分成2组，第一组位置1对应2，位置2对应4，位置3对应5
     *         第二组位置1对应3，位置2对应6，位置3对应7
     */
    // private List<Map<Integer, Integer>> deMergeGroup;
    private Integer groupSize;

    /**
     * 初始积分
     */
    private Long initPoint;

    /**
     * 出局人奖励
     */
    private Long rewardMember;
    /**
     * 出局人推荐人奖励
     */
    private Long rewardParent;
}
