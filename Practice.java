import java.util.HashMap;
import java.util.Map;

public class Practice {

    Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();                    // 1. 키 : Currency와 밸류 : List<Transaction>인 해쉬맵 형성

    for (Transaction transaction : transactions) {                                                  // transactions 배열에 있는 요소를 transaction에 각각 저장!
        Currency currency = transaction.getCurrency();                                              // 뽑아낸 transaction에서 getCurrency() 메소드를 실행하여 Currency 자료형의 currency로 지정
        List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);         // currency에 의해 형성되는 트랜잭션을 모아 transactionsForCurreny 리스트를 만듦

        if(transactionsForCurrency == null) {                                                       // 만약 transactionsForCurreny이 null값이 나온다면?
            transactionsForCurrency = new ArrayList<>();                                            // 새 배열을 하나 생성한 뒤,
            transactionsByCurrencies.put(currency, transactionsForCurrency);                        // transactionsByCurrencies에 집어넣음
        }
        transactionsForCurrency.add(transaction);                                                   // null이 아닐 때, transaction을 transactionsForCurrency 에 집어넣음
    }
    

    -> 스트림을 이용하여 단 한줄로 요약한 모습!

    Map<Currency, List<Transaction>> transactionsByCurrencies = transaction.stream().collect(groupingBy(Transaction::getCurrency));
    public static void main(String[] args){                                 // => Map 자료형의 transactionByCurrencies를 transation을 스트림을 돌려 getCurrency() 메소드를 돌린 결과물을 그룹화 하여 모음


    }
    
}
