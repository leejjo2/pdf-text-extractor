package com.asposepdftest;

import com.aspose.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@Slf4j
class AsposePdfTestApplicationTests {

    @Test
    void contextLoads() {
        String fileName = "IDS";
        Document document = new Document("src/main/resources/"+fileName+".pdf");

        TextAbsorber textAbsorber = new TextAbsorber();

        TextExtractionOptions options = new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure);
        textAbsorber.setExtractionOptions(options);

//        for (int page = 1; page < document.getPages().size(); page++) {
//            document.getPages().get_Item(page).accept(textAbsorber);
//        }
//
//        String text = textAbsorber.getText();
//        log.info("text >>>> \n{}", text);

        // excel
//        XlsxSaveOptions
        ExcelSaveOptions excelSave = new ExcelSaveOptions();
        excelSave.setFormat(ExcelSaveOptions.ExcelFormat.XLSX);
        excelSave.setMinimizeTheNumberOfWorksheets(true);
        document.save(fileName + ".xlsx", excelSave);

        // word
//        DocSaveOptions docSave = new DocSaveOptions();
//        docSave.setMode(DocSaveOptions.RecognitionMode.Flow);
//
//        docSave.setRelativeHorizontalProximity(2.5f);
//        docSave.setRecognizeBullets(true);
//        document.save(fileName + ".doc", docSave);
    }

}
