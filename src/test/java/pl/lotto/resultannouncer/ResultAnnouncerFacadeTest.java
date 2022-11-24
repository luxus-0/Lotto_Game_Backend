package pl.lotto.resultannouncer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultannouncer.dto.ResultAnnouncerDto;
import pl.lotto.resultchecker.ResultsCheckerFacade;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static java.time.Month.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;

class ResultAnnouncerFacadeTest {

    private final ResultsCheckerFacade resultsCheckerFacade;

    ResultAnnouncerFacadeTest(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    @Test
    @DisplayName("return failed user numbers when user gave incorrect date time draw")
    public void should_return_failed_user_numbers_when_user_gave_incorrect_date_time_draw() {
        // given
        Set<Integer> numbersFromUser = Set.of(12, 23, 45, 11, 90, 50);
        UUID uuid = UUID.fromString("0de5f8c1-e926-48dd-92fb-6652d982426c");

        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration().createModuleForTests(resultsCheckerFacade);
        // when
        ResultAnnouncerDto resultAnnouncer = resultAnnouncerFacade.getResultByUUID(uuid);
        // then
        LocalDateTime datetime = LocalDateTime.of(2022, DECEMBER, 14, 12, 0);

        assertThat(resultAnnouncer.dateTimeWinner()).isNotEqualTo(datetime);
        assertThat(resultAnnouncer.winnerNumbers()).isNotEqualTo(numbersFromUser);
    }

}