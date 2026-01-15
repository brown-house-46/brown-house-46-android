# Design Principal

## Core rules
- Every screen must be wrapped in `SocialTreeTheme`.
- Colors must come from `BrownColor` only.
- Spacing must come from `BrownSpacing` only.
- Shapes/radius must come from `BrownShape` only.
- Typography must use `MaterialTheme.typography` (backed by `BrownTypography`).

## Component enforcement
- Buttons: use `BrownPrimaryButton`/`BrownButton` only.
- Inputs: use `BrownTextField`/`BrownPasswordField` only.
- Cards/list containers: use `BrownCard` first.
- Avatars/badges: use `BrownAvatar`/`StatusBadge`/`NotificationBadge`.

## Layout rules
- Screen padding: `BrownSpacing.screenPadding`.
- Section spacing: `BrownSpacing.sectionSpacing`.
- Item spacing: `BrownSpacing.itemSpacing`.
- Button height/radius: keep `BrownButton` defaults (no custom dp overrides).

## Prohibited
- Hard-coded colors/fonts/radius/spacing.
- Direct use of raw Material components (wrap in design system if needed).
- Screen-level overrides for font/color/shape; promote to tokens instead.

## New screen checklist
- `SocialTreeTheme` is applied.
- Background/text/icon colors come from `BrownColor`.
- Padding/spacing uses `BrownSpacing`.
- Buttons/inputs/cards use Brown components.
- Typography uses `MaterialTheme.typography`.
- Shapes use `BrownShape`.

