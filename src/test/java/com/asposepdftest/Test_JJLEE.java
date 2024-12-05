package com.asposepdftest;

import com.aspose.ocr.*;
import com.aspose.pdf.Document;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.TextExtractionOptions;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Slf4j
public class Test_JJLEE {

    @Test
    void pdfTest() {

//        Document document = new Document("src/main/resources/test.pdf");
        Document document = new Document("src/test/resources/test_files/Lv4. FDN.pdf");

        TextAbsorber textAbsorber = new TextAbsorber();

        TextExtractionOptions options = new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure);
        textAbsorber.setExtractionOptions(options);

        for (int page = 1; page < document.getPages().size(); page++) {
            if (page < 5) {
                document.getPages().get_Item(page).accept(textAbsorber);
            }
        }

        String text = textAbsorber.getText();
        log.info("text >>>> \n{}", text);

    }

    @Test
    void ocrTest() throws AsposeOCRException {

        // Aspose OCR API 초기화
        AsposeOCR api = new AsposeOCR();

        // OCR 입력 설정 (PDF 지원)
        OcrInput input = new OcrInput(InputType.SingleImage);
        input.add("src/test/resources/test_files/5_python-ocr.jpg"); // PDF 파일 경로 추가

        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setLanguage(Language.Eng);

        // 텍스트 인식 수행
        ArrayList<RecognitionResult> results = api.Recognize(input, recognitionSettings);

        // 결과 출력
        for (RecognitionResult result : results) {
            log.info("result >>>>> \n {}", result.recognitionText); // 추출된 텍스트 출력
        }

    }

//    @Test
//    void pdfOcrTest(){
//        // Aspose OCR API 초기화
//        AsposeOCR api = new AsposeOCR();
//
//        // OCR 입력 설정 (PDF 지원)
//        OcrInput input = new OcrInput(InputType.SingleImage);
//        input.add("src/test/resources/test_files/5_python-ocr.jpg"); // PDF 파일 경로 추가
//
//        RecognitionSettings recognitionSettings = new RecognitionSettings();
//        recognitionSettings.setLanguage(Language.Eng);
//
//        // 텍스트 인식 수행
//        ArrayList<RecognitionResult> results = api.Recognize(input, recognitionSettings);
//
//        // 결과 출력
//        for (RecognitionResult result : results) {
//            log.info("result >>>>> \n {}", result.recognitionText); // 추출된 텍스트 출력
//        }
//
//    }

    @Test
    void tesseractTest() throws TesseractException, FileNotFoundException, IOException {
        // Tesseract 객체 생성
        Tesseract tesseract = new Tesseract();

        // Tesseract 언어 파일 경로 설정 (기본적으로 eng.traineddata 파일이 필요)
        tesseract.setDatapath("src/test/resources/tessdata");  // macOS의 경우

        // 테스트용 파일 경로 (PDF 파일)
        File inputFile = new File("src/test/resources/test_files/Lv4. FDN.pdf");  // PDF 파일 경로

        BufferedImage image = null;

        // 입력 파일이 PDF라면 이미지로 변환
        if (inputFile.getName().endsWith(".pdf")) {
            PDDocument document = PDDocument.load(inputFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 첫 번째 페이지 이미지만 추출
            image = pdfRenderer.renderImageWithDPI(0, 300);  // 첫 번째 페이지를 300 DPI로 렌더링

            document.close();
        } else {
            // 이미지를 파일에서 읽기
            image = ImageIO.read(inputFile);
        }

        if (image != null) {
            // Tesseract로 텍스트 추출
            String extractedText = tesseract.doOCR(image);

            // 텍스트 추출 결과 검증
            System.out.println("OCR 결과: " + extractedText);

            // 추출된 텍스트에 "Hello"가 포함되어 있는지 확인
            Assertions.assertTrue(extractedText.contains("Hello"));
        } else {
            System.err.println("유효한 이미지 또는 PDF 파일이 아닙니다.");
        }
    }

    @Test
    void easyOCRTest() {
        try {
            // Python 스크립트 실행 명령어 설정
            String pythonScriptPath = "src/test/resources/py_scripts/ocr_script.py";  // 기존 Python 스크립트 경로
//            String pdfPath = "src/test/resources/test_files/Lv4. FDN.pdf";  // PDF 파일 경로
            String pdfPath = "src/test/resources/test.pdf";  // PDF 파일 경로

            ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath, pdfPath);

            // 표준 출력과 표준 에러 통합 (파이썬 로그를 모두 출력)
            pb.redirectErrorStream(true);  // stderr를 stdout에 합침
            Process process = pb.start();

            // Python 출력 결과 읽기 (stdout + stderr)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.lines().collect(Collectors.joining("\n"));
            log.info("OCR 결과 및 로그: ");
            log.info(result);

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            log.info("프로세스 종료 코드: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
