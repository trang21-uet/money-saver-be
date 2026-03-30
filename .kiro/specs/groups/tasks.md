# Tasks - Chia Sẻ Chi Tiêu Nhóm

- [ ] 1. Implement Group management (Backend)
  - [ ] 1.1 Tạo GroupService và GroupController
    - CRUD, generate invite code, thêm/xóa members, tính group balances, settle payments
    - _Yêu cầu: 8.1, 8.2, 8.4, 8.5, 8.6, 8.8_
  - [ ] 1.2 Tích hợp group transactions với TransactionService
    - Giao dịch có group_id → cập nhật group balances, gửi notification đến members
    - _Yêu cầu: 8.3, 8.4, 8.7_
  - [ ]\* 1.3 Viết tests cho group balance calculations
    - Test split calculations, settle payment logic
    - _Yêu cầu: 8.4, 8.6_

- [ ] 2. Implement Group screens (Flutter)
  - [ ] 2.1 Implement GroupBloc và GroupRepository
    - _Yêu cầu: 8.1, 8.2, 8.8_
  - [ ] 2.2 Tạo group screens
    - Group list, create group, group detail (members + balances + transactions), settle payment
    - _Yêu cầu: 8.1, 8.3, 8.5, 8.6_
  - [ ]\* 2.3 Viết tests cho group balance calculations (Flutter)
    - _Yêu cầu: 8.4, 8.6_
