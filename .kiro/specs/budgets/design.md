# Thiết Kế - Lập Kế Hoạch Chi Tiêu (Budget)

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `budgets`)

## API Endpoints

```
GET    /api/budgets                - Danh sách ngân sách
GET    /api/budgets/{id}           - Chi tiết ngân sách
POST   /api/budgets                - Tạo ngân sách mới
PUT    /api/budgets/{id}           - Cập nhật ngân sách
DELETE /api/budgets/{id}           - Xóa ngân sách
GET    /api/budgets/{id}/progress  - Tiến độ sử dụng ngân sách
```

## Backend Components

**BudgetService**:

- CRUD operations cho budgets
- `calculateUsagePercentage(budgetId)`: tổng chi tiêu theo category trong khoảng thời gian / budget amount \* 100
- Trigger notification khi vượt 80% và 100%
- Scheduled job kiểm tra định kỳ sau mỗi giao dịch mới

## Flutter Components

**BudgetBloc + BudgetRepository**

**Screens**:

- Budget list: progress bars cho từng budget
- Add/edit budget: chọn category, amount, period (daily/weekly/monthly/yearly), date range
- Budget detail: spending breakdown theo category
