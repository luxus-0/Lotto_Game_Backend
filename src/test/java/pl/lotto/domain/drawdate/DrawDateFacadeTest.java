package pl.lotto.domain.drawdate;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DrawDateFacadeTest {

    @Test
    public void should_return_fail_date_when_date_is_before_draw_date() {
        //given
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2023, 12, 7, 12, 0, 0);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 13, 0, 0).plusHours(12).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration().drawDateFacade(clock);
        //when
        LocalDateTime actualNextDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertFalse(actualNextDrawDate.isEqual(expectedNextDrawDate));
    }

    @Test
    public void should_return_fail_date_when_date_is_after_draw_date() {
        //given
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2023, 12, 7, 12, 0, 0);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 13, 0, 0).plusHours(12).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration().drawDateFacade(clock);
        //when
        LocalDateTime actualNextDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualNextDrawDate).isAfter(expectedNextDrawDate);
    }

    @Test
    public void should_return_success_date_when_date_is_equal_draw_date() {
        //given
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2023, 12, 23, 12, 0, 0);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 17, 0, 0).plusHours(12).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration().drawDateFacade(clock);
        //when
        LocalDateTime actualNextDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualNextDrawDate).isEqualTo(expectedNextDrawDate);
    }

    @Test
    public void should_throw_an_exception_when_date_is_before_draw_date() {
        //given
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2023, 12, 14, 11, 0, 0);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 30, 0, 0, 0).plusHours(12).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration().drawDateFacade(clock);
        //when
        LocalDateTime actualNextDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThrows(NextDrawDateNotFoundException.class, () -> {
            if (expectedNextDrawDate.isBefore(actualNextDrawDate)) {
                throw new NextDrawDateNotFoundException("Draw date time: " + actualNextDrawDate.toLocalDate() + actualNextDrawDate.toLocalTime());
            }
        });
    }

    @Test
    public void should_throw_an_exception_when_date_is_after_draw_date() {
        //given
        LocalDateTime expectedNextDrawDate = LocalDateTime.of(2023, 12, 30, 11, 0, 0);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 14, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration().drawDateFacade(clock);
        //when
        LocalDateTime actualNextDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThrows(NextDrawDateNotFoundException.class, () -> {
            if (expectedNextDrawDate.isAfter(actualNextDrawDate)) {
                throw new NextDrawDateNotFoundException(
                        "Draw date time: " + actualNextDrawDate.toLocalDate() + actualNextDrawDate.toLocalTime());
            }
        });
    }
}