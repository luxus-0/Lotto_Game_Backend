package pl.lotto.emailsender;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.List;

@Service
class EmailReader {

    public static final String FILE_NAME = "Email.csv";

        String readPasswordFromCSV() throws Exception {
        FileReader fileReader = new FileReader(FILE_NAME);

        CSVReader csvReader = new CSVReader(fileReader);
        List<String[]> readAllLines = csvReader.readAll();
        return readAllLines.stream()
                .findFirst()
                .map(String::valueOf)
                .orElseThrow();
    }

    public static void main(String[] args) throws Exception {
        EmailReader emailPasswordReader = new EmailReader();
        String password = emailPasswordReader.readPasswordFromCSV();
        System.out.println(password);
    }
}
