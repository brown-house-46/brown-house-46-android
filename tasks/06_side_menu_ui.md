# Issue #6: 사이드 메뉴 UI

## 개요
하단 네비게이션 바에 Menu 버튼을 추가하고, 좌측에서 슬라이드되는 사이드 메뉴 패널 구현

## 세부 태스크

### 1. BottomNavBar 수정
- [ ] Menu 버튼을 맨 좌측에 추가
  - [ ] Icons.Default.Menu 아이콘 사용
  - [ ] onOpenMenu 콜백 파라미터 추가
- [ ] 기존 버튼 순서 조정: Menu, Tree, List, Settings

### 2. 사이드 메뉴 상태 관리
- [ ] HomeScreen에 DrawerState 추가
- [ ] rememberDrawerState(initialValue = DrawerValue.Closed) 사용
- [ ] coroutineScope로 drawer 열기/닫기 제어
- [ ] onOpenMenu 콜백에서 drawerState.open() 호출

### 3. SideMenuDrawer 컴포넌트 생성
- [ ] 새 파일: socialtree/feature/home/components/SideMenuDrawer.kt
- [ ] ModalNavigationDrawer 사용
- [ ] DrawerState 파라미터 추가
- [ ] gesturesEnabled = true 설정
- [ ] scrimColor: BrownColor.TextMainLight.copy(alpha = 0.4f)

### 4. SideMenuHeader 구현
- [ ] 배경색: BrownColor.SurfaceVariant (Light) / 다크모드 대응
- [ ] 프로필 사진
  - [ ] BrownAvatar 컴포넌트 재사용 (LARGE 사이즈)
  - [ ] 편집 아이콘 배지 (Primary 색상 원형 배경)
- [ ] 사용자 이름 (22sp, Bold)
- [ ] "View Profile >" 링크 (TextSubLight 색상)
- [ ] 닫기 버튼 (우측 상단, BrownIconButton 사용)
- [ ] 패딩: top=56dp, bottom=32dp, horizontal=24dp

### 5. SideMenuSection 컴포넌트
- [ ] 섹션 타이틀 (SOCIAL, GENERAL)
  - [ ] 대문자, 12sp, Bold, TextSubLight 색상
  - [ ] 상단 마진: 16dp, 하단 마진: 8dp

### 6. SideMenuItem 컴포넌트
- [ ] 높이: 56dp
- [ ] 아이콘 + 텍스트 + 옵션 뱃지 레이아웃
- [ ] 일반 상태: 아이콘/텍스트 TextSubLight
- [ ] 선택/활성 상태:
  - [ ] 배경: Primary.copy(alpha = 0.1f)
  - [ ] 테두리: Primary.copy(alpha = 0.2f)
  - [ ] 아이콘/텍스트: Primary
- [ ] 호버 상태: 배경 SurfaceVariant
- [ ] 라운드: 12dp (BrownShape.medium)

### 7. 메뉴 항목별 구현
- [ ] SOCIAL 섹션
  - [ ] Friends (친구): Icons.Default.Group, 카운트 뱃지 지원
  - [ ] Family Groups: Icons.Default.Diversity1 (또는 유사 아이콘)
  - [ ] My Tree: Icons.Default.AccountTree
- [ ] GENERAL 섹션
  - [ ] Notifications: Icons.Default.Notifications, 알림 점(dot) 뱃지
  - [ ] Settings: Icons.Default.Settings
  - [ ] Help & Support: Icons.Default.Help

### 8. 뱃지 컴포넌트 구현
- [ ] CountBadge (숫자 뱃지)
  - [ ] 배경: Primary
  - [ ] 텍스트: 흰색, 10sp, Bold
  - [ ] 최소 너비: 20dp, 높이: 20dp
  - [ ] 패딩: horizontal=6dp
- [ ] NotificationDot (알림 점)
  - [ ] 크기: 8dp
  - [ ] 색상: Error (또는 red-400)
  - [ ] 아이콘 우측 상단에 절대 위치

### 9. SideMenuFooter 구현
- [ ] 상단 구분선: BorderLight
- [ ] Log Out 버튼
  - [ ] BrownButton (SECONDARY variant) 또는 커스텀
  - [ ] 배경: SurfaceVariant
  - [ ] Icons.Default.Logout 아이콘
  - [ ] 전체 너비, 높이 48dp
- [ ] 앱 버전 텍스트
  - [ ] "Social Tree v2.4.0" 형식
  - [ ] 10sp, TextSubLight, 중앙 정렬
  - [ ] 상단 마진: 16dp
- [ ] 하단 패딩: 40dp (safe area 고려)

### 10. 드로어 스타일링
- [ ] 너비: 화면 너비의 85% (LocalConfiguration 사용)
- [ ] 우측 모서리 라운드: 32dp (RoundedCornerShape)
- [ ] 배경: BackgroundLight / BackgroundDark
- [ ] 그림자 효과

### 11. 애니메이션 처리
- [ ] ModalNavigationDrawer 기본 애니메이션 사용
  - [ ] 슬라이드: 왼쪽에서 오른쪽으로
  - [ ] 페이드: scrim 오버레이
- [ ] 기본 duration: 300ms (Material3 기본값)

### 12. 네비게이션 연결
- [ ] 메뉴 항목 클릭 시 해당 화면으로 이동
  - [ ] Friends -> Friends 화면 (향후 구현)
  - [ ] Settings -> onOpenSettings 콜백 호출
  - [ ] My Tree -> 홈 화면 (현재 화면)
- [ ] 드로어 닫기 후 네비게이션
- [ ] NavController 또는 콜백 방식 선택

### 13. 다크 모드 지원
- [ ] isSystemInDarkTheme() 또는 LocalConfiguration 활용
- [ ] 색상 토큰 분기 처리
  - [ ] 배경: BackgroundLight ↔ BackgroundDark
  - [ ] 텍스트: TextMainLight ↔ TextMainDark
  - [ ] 서피스: SurfaceLight ↔ SurfaceDark
  - [ ] 테두리: BorderLight ↔ BorderDark

### 14. 접근성 (Accessibility)
- [ ] contentDescription 추가
- [ ] 메뉴 항목 터치 영역 최소 48dp
- [ ] 시맨틱 라벨 설정

### 15. 테스트 및 프리뷰
- [ ] SideMenuDrawer Preview 작성
- [ ] SideMenuHeader Preview
- [ ] SideMenuItem Preview (일반/선택 상태)
- [ ] Light/Dark 모드 프리뷰
- [ ] 다양한 화면 크기 테스트

## 참고사항
- Material Design 3 가이드라인 준수
- Jetpack Compose ModalNavigationDrawer 사용
- 기존 디자인 시스템 토큰 재사용 (BrownColor, BrownSpacing, BrownShape)
- 공통 컴포넌트 재사용 (BrownAvatar, BrownIconButton, BrownButton)
- UI 디자인: document/메뉴_모달_페이지 참고
