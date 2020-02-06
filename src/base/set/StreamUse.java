package base.set;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamUse {
    public static void main(String[] args) {
        List<QuestionOption> list = Stream.iterate(0, n -> ++n).limit(6).map(i -> {
            QuestionOption o = new QuestionOption();
            o.setScore(i);
            o.setTitle(i + "分");
            return o;
        }).collect(Collectors.toList());


    }

    public void situation1(List<Dish> dishList) {
        dishList.stream()
                .filter(d -> d.getCalorie() < 400)
                .sorted(Comparator.comparing(Dish::getCalorie))
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    /**
     * 根据名称分组
     *
     * @param dishList
     */
    public void situation2(List<Dish> dishList) {
        dishList.stream()
                .collect(groupingBy(Dish::getName));
        dishList.stream().collect(summingInt(Dish::getCalorie));
        dishList.stream().map(Dish::getCalorie).reduce(0, Integer::sum);
        dishList.stream().mapToInt(Dish::getCalorie).sum();

        dishList.stream().collect(averagingInt(Dish::getCalorie));
        IntSummaryStatistics statistics = dishList.stream().collect(summarizingInt(Dish::getCalorie));
        statistics.getAverage();
        statistics.getCount();
        statistics.getMax();
        statistics.getMin();
        statistics.getSum();
        dishList.stream()
                .map(Dish::getName)
                .collect(Collectors.joining(","));
        // 分区 是特殊的 group,结果最多是2个group
        dishList.stream().collect(partitioningBy(Dish::isVegetatian));
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5);
        numList.stream().collect(partitioningBy(i -> i < 3));
    }

    public void situation3() {
        // 生成 3个偶数，seed：初始化的值
        Stream.iterate(0, n -> n + 2).limit(3);
        Stream.generate(Math::random).limit(3);
        List<String> wordList = Arrays.asList("Hello", "world");
        wordList.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        // 统计
        wordList.stream().count();
        wordList.stream().collect(counting());

    }
}
