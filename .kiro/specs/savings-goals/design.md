# Thiết Kế - Mục Tiêu Tiết Kiệm và Tài Sản

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `savings_goals`, `assets`, `asset_value_history`)

## API Endpoints

```
GET    /api/savings-goals                       - Danh sách mục tiêu
POST   /api/savings-goals                       - Tạo mục tiêu mới
PUT    /api/savings-goals/{id}                  - Cập nhật mục tiêu
DELETE /api/savings-goals/{id}                  - Xóa mục tiêu
POST   /api/savings-goals/{id}/contribute       - Đóng góp vào mục tiêu
POST   /api/savings-goals/{id}/withdraw         - Rút tiền từ mục tiêu

GET    /api/assets                              - Danh sách tài sản
POST   /api/assets                              - Tạo tài sản mới
PUT    /api/assets/{id}                         - Cập nhật tài sản
DELETE /api/assets/{id}                         - Xóa tài sản
GET    /api/assets/net-worth                    - Tổng tài sản ròng
```

## Backend Components

**SavingsGoalService**:

- CRUD operations
- `contribute(goalId, amount, accountId)`: tạo transaction chi tiêu, tăng `current_amount`
- `withdraw(goalId, amount, accountId)`: tạo transaction thu nhập, giảm `current_amount`
- `calculateMonthlySavingsNeeded(goal)`: `(target - current) / monthsUntilDeadline`
- Gửi congratulation notification khi `current_amount >= target_amount`

**AssetService**:

- CRUD operations, lưu lịch sử vào `asset_value_history` khi cập nhật giá trị
- `calculateNetWorth(userId)`: tổng assets + tổng account balances - tổng remaining debts

## Flutter Components

**Screens**:

- Savings goal list: progress bars, % hoàn thành
- Add/edit goal: name, target_amount, deadline, image picker
- Contribute/withdraw screens
- Asset list: total value, breakdown chart theo type
- Net worth screen: assets + accounts - debts
