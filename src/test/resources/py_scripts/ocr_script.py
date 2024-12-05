# pip install easyocr, pdf2image, numpy
# pillow 10 이하 버전 설치 ex) pip install pillow==9.5.0
# conda 환경 사용일 경우 pip 대신 conda
# Poppler 없을 경우 설치
# macOS - brew install poppler / ubuntu - sudo apt-get install poppler-utils
# 빠르게 할려면 gpu 설정

import sys  # 명령행 인수 사용을 위한 라이브러리
import numpy as np  # 이미지 데이터를 배열로 처리
from pdf2image import convert_from_path  # PDF를 이미지로 변환
from PIL import ImageDraw, Image  # 이미지 조작을 위한 라이브러리
import easyocr  # OCR 기능 제공

def extract_text_from_pdf(pdf_path):
    """PDF에서 텍스트를 추출하여 출력하는 함수"""

    # PDF를 이미지로 변환 (첫 페이지만 사용)
    images = convert_from_path(pdf_path)
    image = images[0]  # 첫 번째 페이지만 선택

    # OCR 실행을 위한 EasyOCR 설정 (영어)
    reader = easyocr.Reader(['en'])
    np_image = np.array(image)  # 이미지를 NumPy 배열로 변환
    result = reader.readtext(np_image)  # 텍스트 추출

    # 이미지에 텍스트 표시를 위한 객체 생성
    draw = ImageDraw.Draw(image)

    for (bbox, text, confidence) in result:
        top_left = tuple(bbox[0])
        bottom_right = tuple(bbox[2])

        # 신뢰도가 0.5 이상일 경우만 처리
        if confidence >= 0.5:
            draw.rectangle([top_left, bottom_right], outline="red", width=2)
            draw.text((top_left[0], top_left[1] - 15), text, fill="blue")

        print(f"Text: {text}, Confidence: {confidence}")

    # 이미지에 텍스트 박스가 그려진 결과를 저장
    image.save('annotated_image.jpg')
    print("텍스트가 추출된 이미지가 'annotated_image.jpg'에 저장되었습니다.")

    # 결과를 텍스트 파일로 저장
    with open('ocr_result.txt', 'w', encoding='utf-8') as f:
        for _, text, confidence in result:
            if confidence >= 0.5:
                f.write(f"Text: {text}\nConfidence: {confidence}\n\n")
    print("OCR 결과가 'ocr_result.txt'에 저장되었습니다.")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("사용법: python script_name.py <PDF 파일 경로>")
    else:
        pdf_path = sys.argv[1]  # 명령행 인수에서 PDF 경로 받기
        print(f"경로: {pdf_path}")
        extract_text_from_pdf(pdf_path)