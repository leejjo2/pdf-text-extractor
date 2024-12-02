package com.asposepdftest;

import com.aspose.pdf.Document;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.TextExtractionOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@Slf4j
class AsposePdfTestApplicationTests {

    @Test
    void contextLoads() {
        Document document = new Document("src/main/resources/test.pdf");

        TextAbsorber textAbsorber = new TextAbsorber();

        TextExtractionOptions options = new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure);
        textAbsorber.setExtractionOptions(options);

        for (int page = 1; page < document.getPages().size(); page++) {
            document.getPages().get_Item(page).accept(textAbsorber);
        }

        String text = textAbsorber.getText();
        log.info("text >>>> \n{}", text);

    }

}
