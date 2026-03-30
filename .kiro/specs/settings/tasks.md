# Tasks - Cài Đặt và Thông Báo

- [ ] 1. Implement Notification Services (Backend)
  - [ ] 1.1 Tạo NotificationService với Firebase Cloud Messaging
    - Templates cho budget alert, debt reminder, payment reminder, group activity, savings goal
    - _Yêu cầu: 4.2, 4.3, 5.4, 6.2, 8.7, 12.6_
  - [ ] 1.2 Tạo EmailService
    - Password reset email, group invitation email
    - _Yêu cầu: 8.1, 15.7_
  - [ ]\* 1.3 Viết tests cho notification delivery
    - Mock Firebase và email service, test templates
    - _Yêu cầu: 4.2, 6.2_

- [ ] 2. Implement Settings screens (Flutter)
  - [ ] 2.1 Tạo settings screen
    - Language, currency, theme, week start day, date format, notification preferences
    - _Yêu cầu: 19.1, 19.2, 19.3, 19.4, 19.5, 19.6_
  - [ ] 2.2 Implement localization với intl package
    - Translation files cho vi/en
    - _Yêu cầu: 19.1_
  - [ ] 2.3 Implement theme switching
    - Light/dark/auto theme, persist preference
    - _Yêu cầu: 19.3_

- [ ] 3. Implement Push Notifications (Flutter)
  - [ ] 3.1 Setup Firebase Cloud Messaging
    - Configure Firebase, add FCM dependencies, request permissions
    - _Yêu cầu: (notifications)_
  - [ ] 3.2 Handle notification events
    - Foreground/background/tap actions, navigate to relevant screen
    - _Yêu cầu: (notifications)_
