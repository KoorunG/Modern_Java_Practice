public class MakeCollectorInter {


    Supplier<A> supplier();                         //1. 새로운 결과 컨테이너 만들기

    // public Supplier<List<T>> supplier() {
    //     return () -> new ArrayList<T>();
    // }


    BiConsumer<A,T> accumulator();                  //2. 결과 컨테이너에 요소 추가하기
    
    // public BiConsumer<List<T>, T> accumulator() {
    //     return (list, item) -> list.add(item);
    // }

    Function<A,R> finisher();                       //3. 최종 변환값을 결과 컨테이너로 적용하기

    // public Function<List<T>, List<T>> finisher() {
    //     return Function.identity();
    // }

    BinaryOperator<A> combiner();                   //4. 두 결과 컨테이너 병합 (서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의)

    // public BinaryOperator<List<T>> combiner(); {
    //     return (list1, list2) -> { 
    //         list1.addAll(list2);
    //         return list1;
    //     }
    // }

    Set<Characteristics> characteristics();         //5. 컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반환 (열거형)
                                                    // UNORDERED -> 리듀싱의 결과는 순서에 영향을 받지 않음
                                                    // CONCURRENT -> 다중쓰레드, 스트림의 병렬 리듀싱 수행 (UNOREDERED가 설정되지 않았다면, 정렬되지 않은 상황에서만 병렬 리듀싱 수행 가능)
                                                    // IDENTITY_FINISH -> 리듀싱과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다. ( A -> R로 안전하게 형변환 가능 )
     
                                                    
    // public Set<Characteristics> characteristics() {
    //     return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
    // }                                                 
    

}
