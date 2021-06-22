package git_Laptop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.Map.entry;
import java.security.NoSuchAlgorithmException;

public class ProcessMap {

    public static void main(String[] args) throws NoSuchAlgorithmException {
    
    Map<String, Integer> ageOfFriends = Map.ofEntries(entry ("Raphael", 30) , entry("Olivia" , 25), entry("Thibaut", 26));
    System.out.println(ageOfFriends);

    for(Map.Entry<String, Integer> entry : ageOfFriends.entrySet()) {
        String friend = entry.getKey();
        Integer age = entry.getValue();
        System.out.println(friend + " is " + age + " years old");
     }
     System.out.println(" ===================================== ");

     ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old")); // forEach(BiConsumer(K,V) -> void)

     System.out.println(" ===================================== ");

     Map<String, String> favourateMovies = Map.ofEntries(entry("Raphae", "Star Wars"), entry("Olivia", "James Bond"), entry("Cristina", "Matrix"));
     System.out.println(" = ComparingByValue() = ");
     favourateMovies.entrySet().stream().sorted(Entry.comparingByValue()).forEachOrdered(System.out::println);
     System.out.println(" = ComparingByKey() = ");
     favourateMovies.entrySet().stream().sorted(Entry.comparingByKey()).forEachOrdered(System.out::println);

     System.out.println();
     System.out.println(" = getOrDefault = ");
     System.out.println(favourateMovies.getOrDefault("Olivia", "Matrix"));
     System.out.println(favourateMovies.getOrDefault("Thibaut", "Matrix"));

     System.out.println();
     System.out.println(" = Merge = ");

     Map<String, String> family = Map.ofEntries(entry("Teo", "Star Wars"), entry("Cristina", "James Bond"));
     Map<String, String> friends = Map.ofEntries(entry("Raphael", "Star Wars"), entry("Cristina", "Matrix"));

     Map<String, String> everyone = new HashMap<>(family);
     friends.forEach((k,v) -> everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));//merge(key, value, 중복된 값이 있을 때 remapping function)
     System.out.println(everyone);

     Map<String, Long> moviesToCount = new HashMap<>();    
     String movieName = "James Bond";
     moviesToCount.merge(movieName, 1L, (key,count) -> count + 1L);
     

    //  String friend = "Raphael";
    //  List<String> friendsToMovies = Arrays.asList("Star Wars", "Jack Reaper", "Koorung");
    //  List<String> movies = friendsToMovies.get(friend);
    //  if(movies == null){
    //      movies = new ArrayList<>();
    //      friendsToMovies.put(friend,movies);
    //  }
    //  movies.add("Star Wars");

    //  System.out.println(friendsToMovies);

    //  friendsToMovies.computeIfAbsent("Raphael", name-> new ArrayList<>()).add("Star Wars");




}

}


