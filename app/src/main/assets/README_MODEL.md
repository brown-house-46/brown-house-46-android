# MobileFaceNet 모델 설정 가이드

이 앱의 얼굴 클러스터링 기능을 사용하려면 MobileFaceNet TFLite 모델 파일이 필요합니다.

## 모델 파일 요구사항

- **파일명**: `MobileFaceNet.tflite`
- **위치**: 이 디렉토리 (`app/src/main/assets/`)
- **입력**: 112x112x3 RGB 이미지
- **출력**: 192차원 벡터

## 모델 다운로드 방법

### 옵션 1: 사전 학습된 모델 사용

다음 소스에서 MobileFaceNet TFLite 모델을 다운로드할 수 있습니다:

1. **TensorFlow Model Garden**
   - URL: https://github.com/tensorflow/models
   - 얼굴 인식 모델 섹션 참조

2. **GitHub 오픈소스 프로젝트**
   - 검색: "MobileFaceNet tflite"
   - 예: https://github.com/sirius-ai/MobileFaceNet_TF

3. **Kaggle Datasets**
   - URL: https://www.kaggle.com/
   - 검색: "MobileFaceNet tflite"

### 옵션 2: 직접 변환

1. PyTorch/TensorFlow 모델 다운로드
2. TFLite 포맷으로 변환:
   ```python
   import tensorflow as tf

   converter = tf.lite.TFLiteConverter.from_saved_model('model_path')
   tflite_model = converter.convert()

   with open('MobileFaceNet.tflite', 'wb') as f:
       f.write(tflite_model)
   ```

## 모델 파일 추가 방법

1. `MobileFaceNet.tflite` 파일을 다운로드
2. 이 디렉토리에 복사:
   ```
   app/src/main/assets/MobileFaceNet.tflite
   ```
3. 프로젝트 리빌드
4. 앱 실행

## 모델 없이 실행

모델 파일이 없어도 앱은 정상적으로 실행됩니다.
단, 다음 기능만 사용 가능합니다:

- ✅ 얼굴 개수 검출
- ❌ 얼굴 클러스터링 (동일 인물 그룹화)
- ❌ CSV/JSON 내보내기

앱 실행 시 모델 상태가 UI에 표시됩니다.

## 라이선스 주의사항

MobileFaceNet 모델을 사용할 때는 해당 모델의 라이선스를 확인하세요.
일부 모델은 상업적 사용이 제한될 수 있습니다.

## 문제 해결

### "모델 로드 실패" 오류
- 파일명이 정확히 `MobileFaceNet.tflite`인지 확인
- 파일이 `assets/` 디렉토리에 있는지 확인
- Clean & Rebuild 실행

### "앱이 크래시됨"
- Logcat에서 오류 메시지 확인
- 모델 파일의 입력/출력 형식 확인
- 모델 파일 삭제 후 기본 모드로 실행

## 참고 자료

- ML Kit Face Detection: https://developers.google.com/ml-kit/vision/face-detection
- TensorFlow Lite: https://www.tensorflow.org/lite
- MobileFaceNet 논문: https://arxiv.org/abs/1804.07573
