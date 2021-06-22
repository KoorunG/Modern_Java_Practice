package git_Laptop;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PracSpliterator {

    public int countWordsIteratively(String s){             //문자열 s 인수로 받음
        int counter = 0;
        boolean lastSpace = true;                           //counter과 , boolean lastSpace 초기화
        for (char c : s.toCharArray()) {                    //문자열 s를 toCharArray() 메소드로 분해한 뒤, c에 집어넣고
            if(Character.isWhitespace(c)){                  //isWhitespace(c) -> 지정한 문자가 자바에 따라 공백인지 여부를 결정! (공백일 경우 true, 아닐 경우 false)
                lastSpace = true;
            } else {
                if(lastSpace)
                counter++;                                  
                lastSpace = false;
            }
        }
        return counter;                                     //counter 반환 
    }

    
    
}

class WordCounter {
    private final int counter;                      // counter과 lastSpace를 고정값으로 정의
    private final boolean lastSpace;
    public WordCounter (int counter, boolean lastSpace){
        this.counter = counter;
        this.lastSpace = lastSpace;                 //두 인수를 파라미터로 받는 생성자 정의
    }

    public WordCounter accumulate(Character c) {    //accumulate(Character c) 메소드 정의 
        if (Character.isWhitespace(c)){             //문자열의 공백이 맞다면?
            return lastSpace ?                      //lastSpace가 맞다면 -> this 반환 아니면 WordCounter(counter, true) 객체를 생성하여 반환 
            this : new WordCounter(counter, true);
        } else {
            return lastSpace ? new WordCounter(counter+1, false) : this; //lastSpace가 맞다면 (공백을 만난다면) -> 
                                                                         //WordCounter(counter+1, false) 객체를 생성하여 반환(지금까지 탐색한 문자열을 단어로 간주하여 counter+1) 
                                                                         //아니라면 this 반환 
        }
    }

    public WordCounter combine(WordCounter wordCounter) {               //WordCounter 자료형의 wordCounter을 인수로 받는 combine() 메소드 
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace); // 두 WordCounter의 counter값을 더한다.
    }
    public int getCounter(){
        return counter;
    }

    private int countWords (Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0,true),WordCounter::accumulate, WordCounter::combine);
        return wordCounter.getCounter();
    }

}







class WordCounterSpliterator implements Spliterator<Character> {        //Spliterator -> tryAdvance, trySplit, estimateSize, characteristics 네가지 메소드를 구현해야함
                                                                        // 1. tryAdvance = 남은 요소가 있으면(현재 요소를 소모하여) 해당 요소에 대해 지정된 작업을 수행한 뒤 true 반환
                                                                                         //남은 요소가 없으면 false 반환
                                                                                         
                                                                        // 2. trySplit = '분할'하여 Spliterator 생성 (모든 Spliterator이 충분히 작으면 null값 반환 -> trySplit 종료)
                                                                        // 3. estimateSize = 메소드로 탐색해야 할 요소 수 정보를 제공
                                                                        // 4. characteristics = Spliterator 특성 알려줌             
    private final String string;
    private int currentChar = 0;
    public WordCounterSpliterator(String string){
        this.string = string;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        return currentChar < string.length();

    }
    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if( currentSize < 10) {
            return null;
        }
        for (int splitPos = currentSize / 2 + currentChar;
                splitPos < string.length(); splitPos++) {
                    if(Character.isWhitespace(string.charAt(splitPos))) {
                        Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
                        currentChar = splitPos;
                        return spliterator;
                    }
                }
                return null;
    }
    
    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
    
}

