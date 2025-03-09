package cn.lycodeing.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedHashSet;


@Slf4j
public class ModuloTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Long> {
    /**
     * 根据分片键值执行等值分片路由
     *
     * @param tableNames 所有可用分表名的集合，需要遍历匹配的目标表集合
     * @param shardingValue 分片键值对象，包含用于分片计算的Long类型数值
     * @return 匹配的分表名称，返回符合分片计算规则的具体表名
     *
     * 实现逻辑：
     * 1. 遍历所有候选表名，寻找与分片值计算结果匹配的表
     * 2. 分片计算规则：对分片值取模3，匹配表名最后一位数字
     * 3. 找不到匹配表时抛出非法参数异常
     */
    @Override
    public String doEqualSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue) {
        // 遍历所有候选表进行匹配查找
        for (String each : tableNames) {
            if (each.endsWith(shardingValue.getValue() % 3 + "")) {
                log.info("分片键值：{}，匹配分表：{}", shardingValue.getValue(), each);
                return each;
            }
        }

        // 未找到匹配分表时抛出异常
        throw new IllegalArgumentException();
    }


    /**
     * 根据分片值筛选匹配的目标表名集合
     *
     * @param tableNames 原始表名集合，需要被筛选的候选表列表
     * @param shardingValue 分片键值对象，包含需要用于分片计算的长整型数值集合
     * @return 符合分片路由规则的最终表名集合。返回结果保持原始表名顺序并自动去重
     */
    @Override
    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());

        // 遍历分片键的所有数值进行表名匹配
        for (Long value : shardingValue.getValues()) {
            // 计算分片后缀：取模3运算结果作为表名后缀匹配条件
            final String expectedSuffix = value % 3 + "";

            // 筛选以指定分片后缀结尾的表名
            for (String tableName : tableNames) {
                if (tableName.endsWith(expectedSuffix)) {
                    result.add(tableName);
                }
            }
        }
        log.info("分片键值：{}，匹配分表：{}", shardingValue.getValue(), result);
        return result;
    }


    @Override
    public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : tableNames) {
                if (each.endsWith(i % 3 + "")) {
                    result.add(each);
                }
            }
        }
        log.info("分片键值：{}，匹配分表：{}", shardingValue.getValue(), result);
        return result;
    }
}
