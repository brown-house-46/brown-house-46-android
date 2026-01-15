# Issue #6: 홈 화면 트리 캔버스 팬/줌 기능

## 개요
홈 화면의 트리 캔버스에 드래그(팬)와 확대/축소(줌) 제스처 기능 추가

## 세부 태스크

### 1. 기본 변환 인프라
- [x] TreeCanvas에 상태 변수 추가 (scale, offsetX, offsetY)
- [x] 내부 콘텐츠를 Box로 감싸고 graphicsLayer 적용
- [x] clipToBounds()로 영역 제한

### 2. 제스처 핸들러
- [x] pointerInput + detectTransformGestures 구현
- [x] 핀치 줌 처리 (줌 중심점 보정)
- [x] 드래그 팬 처리
- [x] 줌 범위 제한 (0.5x ~ 3.0x)

### 3. 더블탭 줌
- [x] detectTapGestures로 더블탭 감지
- [x] 더블탭 시 1.0x <-> 2.0x 토글
- [x] 탭 위치 기준 줌

### 4. 검증
- [x] 빌드 성공 확인
- [ ] 노드 클릭 이벤트 정상 작동 확인 (수동 테스트 필요)
- [ ] 배경 그리드, 연결선 함께 이동/확대 확인 (수동 테스트 필요)
- [ ] 줌/팬 범위 제한 확인 (수동 테스트 필요)

## 수정 파일
- `feature/home/HomeScreen.kt` - TreeCanvas 컴포저블 수정

## 참고사항
- Jetpack Compose pointerInput API 사용
- detectTransformGestures로 멀티터치 제스처 처리
- graphicsLayer로 변환 적용 (성능 최적화)
