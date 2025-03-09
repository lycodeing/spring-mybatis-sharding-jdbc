package cn.lycodeing.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedHashSet;

@Slf4j
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {
    @Override
    public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        for (String each : databaseNames) {
            if (each.endsWith(Integer.parseInt(shardingValue.getValue().toString()) % 2 + "")) {
                log.info("分片键值：{}，匹配分库：{}", shardingValue.getValue(), each);
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        for (Integer value : shardingValue.getValues()) {
            for (String tableName : databaseNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        log.info("分片键值：{}，匹配分库：{}", shardingValue.getValue(), result);
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        Range<Integer> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : databaseNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        log.info("分片键值：{}，匹配分库：{}", shardingValue.getValue(), result);
        return result;
    }
}
