package pl.lotto.numbersgenerator;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
class NumbersGeneratorFacadeConfiguration {
    NumbersGeneratorFacade createModuleForTests(NumbersGeneratorRepository numbersGeneratorRepository) {
        NumbersGeneratorValidator numbersGeneratorValidator = new NumbersGeneratorValidator();
        return new NumbersGeneratorFacade(numbersGeneratorRepository, numbersGeneratorValidator);
    }
}
