package git_Laptop;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import static java.util.stream.Collector.Characteristics.*;
import static java.util.stream.Collectors.*;



public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double)candidate);
        return primes.stream().takeWhile(i -> i <= candidateRoot).noneMatch(i -> candidate % i == 0);
    }

    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> new HashMap<Boolean, List<Integer>>(){
            {
                put(true, new ArrayList<Integer>());
                put(false, new ArrayList<Integer>());
            }
        };
    }

    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
            acc.get(isPrime(acc.get(true), candidate)).add(candidate);
        };
    }

    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
    }

    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH)); // import static java.util.stream.Collector.Characteristics.*; 처럼 스태틱 메소드를 import 해줘야 단독으로 인식함
    }
    
}

class CollectorHarness {

    public static boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt((double)candidate);
        return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
    }

   public static Map<Boolean,List<Integer>> partitionPrimes(int n) {

    return IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(candidate -> isPrime(candidate)));
   }

   public static Map<Boolean,List<Integer>> partitionPrimesWithCustomCollectors(int n){
       return IntStream.rangeClosed(2, n).boxed().collect(new PrimeNumbersCollector());
   }
   

    public static void main(String[] args) {
        long fastest = Long.MAX_VALUE;
        for(int i = 0 ; i < 10 ; i++){
            long start = System.nanoTime();
            partitionPrimes(1_000_000);
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (duration < fastest) fastest = duration;

            
        }
        System.out.println("Fastest execution done in " + fastest + " msecs");
    }
    
    public static void main1(String[] args) {
        long fastest = Long.MAX_VALUE;
        for(int i = 0 ; i < 10 ; i++){
            long start = System.nanoTime();
            partitionPrimesWithCustomCollectors(1_000_000);
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (duration < fastest) fastest = duration;

            
        }
        System.out.println("Fastest execution done in " + fastest + " msecs");
    }
}
