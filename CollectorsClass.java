import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class CollectorsClass {
    long howManyDishes = menu.stream().collect(counting());
}

public static <T> Collector<T, ? , long> counting() {
    return reducing(0L, e -> 1L, Long::sum);
} // counting() 구현... long타입을 반환하며, 누적자로 아무 자료형이나 쓸 수 있고, T를 인수로 받는

Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories); // 비교자 구현

Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator)); // Collectors.maxBy(비교자) , Optional<> -> null값이 있을수도 있기 때문에 감싸줌


int totalCalories = menu.stream().collect(summingInt(Dish::getCalories)); // Dish 자료형의 getCalories() 메소드를 수행한 것을 합산한뒤 Int로 맵핑 후 collect() 때문에 int 자료형이 반환값으로 올 수 있다.

IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));

//객체 출력하면? IntSummaryStatistics{count : , sum , min ... 각종 연산결과가 출력된다.}

String shortMenu = menu.stream().map(Dish::getName).collect(joining()); // 추출한 값을 '하나의 문자열'로 묶어서 출력
                                                                        // 내부적으로 StringBuilder을 이용하여 문자열을 하나로 만드는 것임
                                                                        // Dish 클래스의 toString()이 String을 리턴하도록 오버라이딩 되었다면,

String shortMenu = menu.stream().collect(joining());                    //과 같이 쓸 수 있다는거~



-> 팩토리 메소드(이미 특화된 컬렉터)



reducing() -> 범용 메소드

int totalCalories = menu.stream().collect(reducing(0,Dish::getCalories, (i,j) -> i + j ));

                                            // 1번 인수 = 초기값, 2번 인수 = 변환 함수(Function> , 3번 인수 = BinaryOperator(같은 종류의 두 항목을 하나로 더하는))

Optional<Dish> mostCaloriesDish = menu.stream().collect(reducing( (d1,d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)); // 칼로리가 가장 높은 Dish를 출력

int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));

int totalCalories = menu.stream().mapToInt(Dish::getCalories/* ToIntFunction */).sum(); // int로 매핑한 뒤, sum() 최종연산 사용한 것임 -> 오토박싱을 피할수 있다는 좋은 장점

long howManyDishes = menu.stream().collect(counting());

Map<Dish.type, List<Dish>> // 요리의 타입을 넣으면 요리 리스트가 나오는...

Map<Dish.type, List<Dish>> dishByType = menu.stream().collect(groupingBy(Dish::getType));

Map<CaloricLevel, List<Dish>> dishByCaloricLevel = menu.stream().collect(groupingBy ( dish -> {
    if (dish.getCalories() <= 400) return CaloricLevel.DIET; else if (dish.getCalories() <= 700) return CaloricLevel.FAT; else return CaloricLevel.NORMAL;
}));


Map<Dish.type, List<Dish>> caloricDishByType = menu.stream().collect(groupingBy(Dish::getType,mapping(Dish::getName, toList())));

flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())

Map<Dish.type, Map<CaloricLevel, List<Dish>>> /* List로 묶인 음식을 CaloricLevel로 분류 한 뒤, 이를 다시 Dish.type으로 분류!! */ dishesByTypeCaloricLevel = menu.stream().collect(
    groupingBy(Dish::getType, groupingBy(dish -> { 
        if (dish.getCalories() <= 400) 
        return CaloricLevel.DIET; 
        else if(dish.getCalories() <= 700) 
        return CaloricLevel.NORMAL; 
        else return CaloricLevel.FAT })));             // 1차 그룹 -> 칼로리에 따른 타입 분류
                                                       // 2차 그룹 -> 1차 그룹을 음식 유형에 따른 분류


                                                        //-> {MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]}, FISH={DIET ... }} 이런식으로 2차원 분류함! 

Map<Dish.type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));

Map<Dish.type, Dish> mostCaloricByType = menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(comparing(Dish::getCalories)),Optional::get)));


// 메뉴에서 스트림을 실행하여, 음식을 타입으로 분류하여 모을 것이다. 그런데 collectingAndThen 메소드를 이용하여,
// Dish::getCalories를 참조하여 비교 후 가장 높은 것을, Optional::get을 적용하여(즉 , Optional에서 안의 Type을 꺼내오겠다는 뜻) 추출 할 것이다
// 때문에 이 결과로 value값은 Optional로 감쌀 필요가 없다 (Dish임)
                                                        
Map<Dish.Type, Integer> totalCaloriesByType = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));

Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
    if (dish.getCalories() <= 400) return CaloricLevel.DIET; else return CaloricLevel.FAT; 
}, toSet() )));


menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
    if (dish.getCalories() <= 400) return CaloricLevel.DIET; else return CaloricLevel.FAT; 
}, toCollection(HashSet::new /* Collection Factory */ ))));

Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));

// -> {false = [asdfsdf], true = [afsvavwef]}; 가 반환된다! 

List<Dish> vegetarianDishes = partitionedMenu.get(true); // =>  야채인 음식을 반환


Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));

// 그 결과로 두 수준의 맵이 출력된다.

Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = 
menu.stream().collect(partitioningBy(Dish::isVegetarian, collectingAndThen(
    maxBy(comparingInt(Dish::getCalories)), Optional::get)));               //채식주의자 여부로 분할한 뒤, collectingAndThen 메소드를 이용하여, 음식에서 칼로리를 추출해 comparingInt로 비교한 뒤 최댓값을 구하고, 이를 Optional::get 으로 처리하여 Dish값을 추출함



    public boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt((double)candidate);
        return IntStream.range(2, candidate).noneMatch(i -> candidate % i == 0);
    }

    //여기서 만든 isPrime 메소드를 Predicate 자리에 넣어서 써먹을 수 있다!

   Map<Boolean,List<Integer>> partitionPrimes(int n) = IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(candidate -> candidate.isPrime(candidate)));
    




   // 이 모든 Collectors 클래스는 Collector Interface를 구현했다...!
    