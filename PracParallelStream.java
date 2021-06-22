package git_Laptop;

import java.util.stream.Stream;

public class PracParallelStream {

    public long sequentialSum(long n){
        return Stream.iterate(1L, i -> i+1).limit(n).reduce(0L, Long::sum);
    }   // 무한스트림을 만든 뒤 .limit(n) 으로 쇼트서킷 형성
    
    public long parallelSum(long n) {
        return Stream.iterate(1L, i -> i+1).limit(n).parallel().reduce(0L, Long::sum);
    }   // .parallel() : 순차 스트림 -> 병렬 스트림
        // .sequential() : 병렬 스트림 -> 순차 스트림
}
