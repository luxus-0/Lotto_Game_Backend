package pl.lotto.emailsender;

import com.opencsv.CSVReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static pl.lotto.emailsender.FileMessage.FILE_PATH;

@Service
@Log4j2
class EmailReader {

    static String readUsernameAndPasswordFromCSV() throws Exception {
        FileReader fileReader = new FileReader(FILE_PATH);
        CSVReader csvReader = new CSVReader(fileReader);
        List<String[]> readAllLines = csvReader.readAll();
        for (var userAndPasswd : readAllLines)
            if (userAndPasswd.length == 0) {
                log.info("Empty file Email.csv");
            }
        log.info(Arrays.toString(readAllLines.get(0)));
        return "";
    }
}
