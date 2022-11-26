package pl.lotto.resultannouncer;

import org.springframework.context.annotation.Configuration;

@Configuration
class ResultAnnouncerFacadeConfiguration {
    ResultAnnouncerFacade createModuleForTests() {
        return new ResultAnnouncerFacade();
    }
}
