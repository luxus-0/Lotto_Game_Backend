package pl.lotto.numberreceiver;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class Ticket{
   private String hash;
   private Set<Integer> numbers;
   private LocalDateTime drawDate;

   public Ticket(String hash, Set<Integer> numbers) {
      this.hash = hash;
      this.numbers = numbers;
   }

   Ticket dto(){
      return Ticket.builder()
              .numbers(numbers)
              .drawDate(drawDate)
              .build();
   }
}
