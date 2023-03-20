package pl.lotto.domain.resultchecker;

import org.springframework.context.annotation.Configuration;

@Configuration
class ResultsCheckerFacadeConfiguration {
    ResultsCheckerFacade createModuleForTests() {
        NumbersGenerator numbersGenerator = new NumbersGenerator();
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator(numbersGenerator);
        ResultsCheckerRepository resultsCheckerRepository = new InMemoryResultsCheckerRepository();
        return new ResultsCheckerFacade(resultsCheckerValidator, resultsCheckerRepository);
    }
}
