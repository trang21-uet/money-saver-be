# Thiết Kế - Cài Đặt và Thông Báo

> Cài đặt user được lưu trong bảng `users` (shared-architecture/design.md)

## Backend Components

**NotificationService**:

- Gửi push notification qua Firebase Cloud Messaging (FCM)
- Templates cho từng loại: budget_alert, debt_reminder, payment_reminder, group_activity, savings_goal_completed, credit_card_alert
- Kiểm tra user notification preferences trước khi gửi

**EmailService**:

- Gửi email cho password reset, group invitations

**Settings**: Lưu trong bảng `users` (language, currency, theme, week_start_day, date_format, biometric_enabled, auto_backup_enabled, auto_classification_enabled)

## Flutter Components

**Settings screen**:

- Language selector (vi/en) → intl package
- Currency selector (VND/USD/EUR/...)
- Theme selector (light/dark/auto) → dynamic theme switching
- Week start day (Monday/Sunday)
- Date format (DD/MM/YYYY / MM/DD/YYYY)
- Notification preferences: toggle per event type
- Auto backup toggle

**Push Notifications (Firebase Cloud Messaging)**:

- Request notification permissions
- Handle foreground/background/tap notifications
- Navigate to relevant screen khi tap notification
