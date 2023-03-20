package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Configuration;

@Configuration
class NumbersGeneratorFacadeConfiguration {
    NumbersGeneratorFacade createModuleForTests(NumbersGeneratorRepositoryImpl numbersGeneratorRepositoryImpl) {
        NumbersGeneratorValidator numbersGeneratorValidator = new NumbersGeneratorValidator();
        return new NumbersGeneratorFacade(numbersGeneratorValidator, numbersGeneratorRepositoryImpl);
    }
}
