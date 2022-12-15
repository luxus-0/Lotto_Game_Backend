package pl.lotto.numbersgenerator;

import org.springframework.context.annotation.Configuration;

@Configuration
class NumbersGeneratorFacadeConfiguration {
    NumbersGeneratorFacade createModuleForTests(NumbersGeneratorRepository numbersGeneratorRepository) {
        NumbersGeneratorValidator numbersGeneratorValidator = new NumbersGeneratorValidator();
        NumbersGeneratorRepositoryImpl numbersGeneratorImpl = new NumbersGeneratorRepositoryImpl(numbersGeneratorRepository);
        return new NumbersGeneratorFacade(numbersGeneratorValidator, numbersGeneratorImpl);
    }
}
