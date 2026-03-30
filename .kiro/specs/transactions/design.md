# Thiết Kế - Quản Lý Giao Dịch

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `transactions`, `categories`)

## API Endpoints

```
GET    /api/transactions           - Danh sách giao dịch (pagination, filter)
GET    /api/transactions/{id}      - Chi tiết giao dịch
POST   /api/transactions           - Tạo giao dịch mới
PUT    /api/transactions/{id}      - Cập nhật giao dịch
DELETE /api/transactions/{id}      - Xóa giao dịch (soft delete)
GET    /api/transactions/search    - Tìm kiếm giao dịch
POST   /api/transactions/bulk      - Tạo nhiều giao dịch

GET    /api/categories             - Danh sách danh mục
POST   /api/categories             - Tạo danh mục tùy chỉnh
PUT    /api/categories/{id}        - Cập nhật danh mục
DELETE /api/categories/{id}        - Xóa danh mục (với reassignment)
PUT    /api/categories/reorder     - Sắp xếp thứ tự danh mục
```

## Backend Components

**TransactionService**:

- CRUD operations với soft delete
- Tự động cập nhật `current_balance` của account sau mỗi giao dịch
- Transaction rollback nếu update balance thất bại
- Tích hợp ML classification khi tạo giao dịch

**TransactionRepository**: Query với filter (date range, category, account, type), full-text search trên `note`

**CategoryService**:

- Lấy default categories + custom categories của user
- Xóa category: reassign transactions sang category khác trước khi xóa

## Flutter Components

**TransactionBloc**: Quản lý state danh sách, tạo, sửa, xóa giao dịch

**TransactionRepository**: Offline-first - lưu SQLite local trước, sync sau

**Screens**:

- Transaction list: hiển thị theo thời gian giảm dần, lazy loading, pull-to-refresh
- Add/edit transaction: form nhập amount, category, account, date, note, image
- Search/filter: search bar + filter bottom sheet, lưu filter presets
- Category management: list, add/edit với icon picker
