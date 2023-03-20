package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Configuration;

@Configuration
class ResultAnnouncerFacadeConfiguration {
    ResultAnnouncerFacade createModuleForTests() {
        return new ResultAnnouncerFacade();
    }
}
