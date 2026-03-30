# Tasks - Quản Lý Nợ và Nhắc Nhở

- [ ] 1. Implement Debt management (Backend)
  - [ ] 1.1 Tạo DebtService và DebtController
    - CRUD, thanh toán một phần, tính lãi tích lũy
    - _Yêu cầu: 5.1, 5.3, 5.5, 5.6_
  - [ ] 1.2 Implement debt due date reminders
    - Scheduled job: notification trước 3 ngày, 1 ngày, đúng ngày đáo hạn
    - _Yêu cầu: 5.4_

- [ ] 2. Implement Reminder management (Backend)
  - [ ] 2.1 Tạo ReminderService và ReminderController
    - CRUD, tự động tạo reminder tiếp theo cho recurring
    - _Yêu cầu: 6.1, 6.4, 6.5_
  - [ ] 2.2 Implement scheduled notification service
    - Kiểm tra reminders đến hạn, gửi push notification
    - _Yêu cầu: 6.2, 6.4_

- [ ] 3. Implement Debt và Reminder screens (Flutter)
  - [ ] 3.1 Implement DebtBloc, DebtRepository và screens
    - Debt list (payable/receivable), add/edit, payment screen
    - _Yêu cầu: 5.1, 5.2, 5.3, 5.5_
  - [ ] 3.2 Implement ReminderBloc, ReminderRepository và screens
    - Reminder list (upcoming 7 ngày), add/edit với frequency picker, complete action
    - _Yêu cầu: 6.1, 6.3, 6.5, 6.6_
