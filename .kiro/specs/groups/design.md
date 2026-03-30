# Thiết Kế - Chia Sẻ Chi Tiêu Nhóm

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `groups`, `group_members`, `group_balances`)

## API Endpoints

```
GET    /api/groups                 - Danh sách nhóm
POST   /api/groups                 - Tạo nhóm mới
GET    /api/groups/{id}            - Chi tiết nhóm
PUT    /api/groups/{id}            - Cập nhật nhóm
DELETE /api/groups/{id}            - Xóa nhóm
POST   /api/groups/{id}/invite     - Mời thành viên (email hoặc invite code)
POST   /api/groups/{id}/join       - Tham gia nhóm bằng invite code
DELETE /api/groups/{id}/leave      - Rời khỏi nhóm
GET    /api/groups/{id}/balance    - Số dư của từng thành viên
POST   /api/groups/{id}/settle     - Thanh toán trong nhóm
```

## Backend Components

**GroupService**:

- CRUD operations, generate unique invite code
- Thêm/xóa members
- Tính toán group balances sau mỗi giao dịch nhóm
- Settle payments: cập nhật balances khi thành viên thanh toán cho nhau

**Tích hợp với TransactionService**: Giao dịch có `group_id` → tự động cập nhật group balances, gửi notification đến tất cả members

## Flutter Components

**GroupBloc + GroupRepository**

**Screens**:

- Group list
- Create group screen
- Group detail: member list, balances, group transactions
- Settle payment screen
