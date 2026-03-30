# Thiết Kế - Quản Lý Nguồn Tiền

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `accounts`)

## API Endpoints

```
GET    /api/accounts                      - Danh sách nguồn tiền
GET    /api/accounts/{id}                 - Chi tiết nguồn tiền
POST   /api/accounts                      - Tạo nguồn tiền mới
PUT    /api/accounts/{id}                 - Cập nhật nguồn tiền
DELETE /api/accounts/{id}                 - Xóa nguồn tiền (soft delete)
GET    /api/accounts/balance              - Tổng số dư
GET    /api/accounts/{id}/transactions    - Giao dịch theo nguồn tiền (pagination, filter)
GET    /api/accounts/{id}/statistics      - Thống kê thu chi theo nguồn tiền
GET    /api/accounts/savings/interest     - Tính lãi cho tất cả savings accounts
GET    /api/accounts/credit-cards/alerts  - Cảnh báo cho tất cả credit cards
POST   /api/accounts/{id}/pay-debt        - Thanh toán dư nợ thẻ tín dụng
```

## Backend Components

**AccountService**:

- CRUD operations, pessimistic locking cho concurrent balance updates
- `calculateExpectedInterest(account)`:
  ```
  interest = balance * (rate/100) * (daysUntilWithdrawal/365)
  ```
- `getAvailableCredit(account)`: `creditLimit - currentDebt`
- `processExpenseTransaction(transaction)`: tăng `currentDebt` nếu credit card, giảm `currentBalance` nếu loại khác
- `payDebt(accountId, amount)`: giảm `currentDebt`, tạo transaction thanh toán
- `getAccountStatistics(accountId, startDate, endDate)`: tính tổng thu/chi, số dư ròng, biểu đồ theo thời gian

**Scheduled Jobs**:

- `@Scheduled(cron = "0 0 1 * * *")` - Cập nhật expected interest cho savings accounts vào Redis
- `@Scheduled(cron = "0 0 9 * * *")` - Kiểm tra credit card payment due, gửi cảnh báo 3 ngày trước và đúng ngày

## Correctness Properties

- **P1**: `expectedInterest = balance * (rate/100) * (days/365)` ≥ 0
- **P2**: `availableCredit = creditLimit - currentDebt` ≥ 0
- **P3**: Sau giao dịch chi bằng credit card: `newDebt = oldDebt + amount`
- **P4**: Giao dịch bị từ chối nếu `newDebt > creditLimit`
- **P5**: Sau thanh toán: `newDebt = oldDebt - payment`, `payment ≤ oldDebt`
- **P7**: `totalIncome = sum(income transactions)`, `totalExpense = sum(expense transactions)`
- **P8**: `netBalance = totalIncome - totalExpense`

## Flutter Components

**AccountBloc + AccountRepository**: Fetch, create, update, delete accounts; sync với local DB

**Screens**:

- Account list: tổng số dư, danh sách accounts
- Add/edit account: form fields khác nhau theo type (savings: interest_rate, withdrawal_date; credit card: credit_limit, current_debt, payment_due_date)
- Account detail: hiển thị expected_interest (savings) hoặc available_credit + days_until_due (credit card)
- Account statistics: period selector, biểu đồ thu chi, tổng thu/chi/ròng, phân tích theo category
- Credit card payment: form nhập số tiền thanh toán, badge cảnh báo khi gần đến hạn
