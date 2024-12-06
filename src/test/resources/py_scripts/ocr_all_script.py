import easyocr  # OCR 기능 제공
from pdf2image import convert_from_path  # PDF를 이미지로 변환
import numpy as np  # 이미지 데이터를 배열로 처리
from PIL import ImageDraw  # 이미지에 도형을 그리기 위한 도구

# PDF 파일 경로 설정
pdf_path = "/Users/choihyunjun/PythonProject/Lv3. MDS.pdf"

# PDF를 이미지로 변환 (Poppler 경로 추가)
images = convert_from_path(pdf_path)

# EasyOCR 설정
reader = easyocr.Reader(['en'])  # 영어만 인식하도록 설정

# 각 페이지 처리
for page_num, image in enumerate(images, start=1):
    # 이미지를 NumPy 배열로 변환
    np_image = np.array(image)

    # OCR 실행
    result = reader.readtext(np_image)

    # 이미지에 그리기 위한 객체 생성
    draw = ImageDraw.Draw(image)

    # 텍스트 결과를 페이지별로 저장
    with open(f'page_{page_num}_results.txt', 'w', encoding='utf-8') as f:
        for (bbox, text, confidence) in result:
            # 좌표를 정수형으로 변환
            bbox = [[int(x), int(y)] for (x, y) in bbox]

            # 신뢰도가 0.5 이상인 텍스트만 처리
            if confidence >= 0.5:
                # 텍스트 위치에 빨간색 사각형 그리기
                draw.rectangle([*bbox[0], *bbox[2]], outline="red", width=2)
                # 텍스트를 이미지에 파란색으로 쓰기
                # draw.text((bbox[0][0], bbox[0][1] - 15), text, fill="blue")

                # 결과를 텍스트 파일에 저장
                f.write(f"Text: {text}\n")
                f.write(f"Bounding Box: {bbox}\n")
                f.write(f"Confidence: {confidence}\n\n")

    # 페이지 결과 이미지를 저장
    image.save(f'page_{page_num}_result.jpg')