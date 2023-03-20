package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Configuration;

@Configuration
public
class ResultAnnouncerFacadeConfiguration {
    public ResultAnnouncerFacade createModuleForTests() {
        return new ResultAnnouncerFacade();
    }
}
