package git_Laptop;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator extends java.util.concurrent.RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000;

    public ForkJoinSumCalculator(long[] numbers){
        this(numbers,0,numbers.length);             // 바로 밑의 생성자를 받는 지시자 this
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end){
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {                  
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);           // 1. length의 절반만큼 Task를 쪼개는 작업... 
        leftTask.fork();   // RecursiveTask의 상위 클래스인 ForkJoinTask의 메소드이다!                            //(상수로 정한 THRESHOLD값보다 작아질때 까지)
        

        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end);

        Long rightResult = rightTask.compute(); //compute() 메소드를 정의하는 필드 안에 있는데 어떻게 참조 가능한거임??
        Long leftResult = leftTask.join();      //3. 최종적으로 fork() 하여 computeSequentially()한 값들을 join() 하는 과정!

        return leftResult + rightResult;
    }

    private long computeSequentially() {            
        long sum = 0;
        for (int i = start ; i < end ; i++){
            sum += numbers[i];                  //2. 포크 한 이후 순차적 실행 과정 -> i 초기값 : start, i 종료값 : end i++
                                                //sum에 numbers[start] +  .... numbers[end-1]를 순차적으로 더한다.
                                                //sum값을 반환 
        }
        
        return sum;
    }




    public static long forkJoinSum(long n){
        long[] numbers = LongStream.rangeClosed(1, n).toArray();            //인수 n을 입력하면 1 ~ n 까지의 자연수 배열을 만드는 LongStream 형성
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);       //위에서 제작한 ForkJoinSumCalculator에 1~n까지의 numbers 배열 전달
        return new ForkJoinPool().invoke(task);                             //이 task를 ForkJoinPool에 invoke(task)를 통하여 던짐
    }

    
}
