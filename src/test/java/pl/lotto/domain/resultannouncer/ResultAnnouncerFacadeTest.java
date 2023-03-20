package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static java.time.Month.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;

class ResultAnnouncerFacadeTest {

    @Test
    @DisplayName("return failed user numbers when user gave incorrect date time draw")
    public void should_return_failed_user_numbers_when_user_gave_incorrect_date_time_draw() {
        // given
        Set<Integer> numbersFromUser = Set.of(12, 23, 45, 11, 90, 50);
        UUID uuid = UUID.randomUUID();

        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration().createModuleForTests();
        // when
        ResultAnnouncerDto resultAnnouncer = resultAnnouncerFacade.getResults(uuid);
        // then
        LocalDateTime datetime = LocalDateTime.of(2022, DECEMBER, 14, 12, 0);

        assertThat(resultAnnouncer.dateTime()).isNotEqualTo(datetime);
        assertThat(resultAnnouncer.numbersUser()).isNotEqualTo(numbersFromUser);
    }

}