# Thiết Kế - Quản Lý Nợ và Nhắc Nhở

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `debts`, `debt_payments`, `reminders`)

## API Endpoints

```
GET    /api/debts                       - Danh sách khoản nợ
POST   /api/debts                       - Tạo khoản nợ mới
PUT    /api/debts/{id}                  - Cập nhật khoản nợ
DELETE /api/debts/{id}                  - Xóa khoản nợ
POST   /api/debts/{id}/payment          - Thanh toán một phần

GET    /api/reminders                   - Danh sách nhắc nhở
POST   /api/reminders                   - Tạo nhắc nhở mới
PUT    /api/reminders/{id}              - Cập nhật nhắc nhở
DELETE /api/reminders/{id}              - Xóa nhắc nhở
POST   /api/reminders/{id}/complete     - Đánh dấu hoàn thành
```

## Backend Components

**DebtService**: CRUD, cập nhật `remaining_amount` khi thanh toán, tính lãi tích lũy

**ReminderService**: CRUD, tự động tạo reminder tiếp theo cho recurring reminders

**Scheduled Jobs**:

- Kiểm tra debts sắp đến hạn → gửi notification trước 3 ngày, 1 ngày, đúng ngày
- Kiểm tra reminders đến hạn → gửi push notification, tạo reminder tiếp theo

## Flutter Components

**Screens**:

- Debt list: phân biệt payable vs receivable, tổng nợ/cho vay
- Add/edit debt: form với person_name, amount, due_date, interest_rate
- Debt payment screen
- Reminder list: upcoming reminders trong 7 ngày
- Add/edit reminder: frequency picker (daily/weekly/monthly/yearly)
