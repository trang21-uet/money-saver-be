# Tasks - Mục Tiêu Tiết Kiệm và Tài Sản

- [ ] 1. Implement Savings Goals (Backend)
  - [ ] 1.1 Tạo SavingsGoalService và SavingsGoalController
    - CRUD, contribute/withdraw (tạo transaction tương ứng), tính monthly savings needed
    - Notification khi đạt 100%
    - _Yêu cầu: 12.1, 12.2, 12.3, 12.5, 12.6, 12.7_

- [ ] 2. Implement Assets (Backend)
  - [ ] 2.1 Tạo AssetService và AssetController
    - CRUD, lưu lịch sử giá trị vào asset_value_history
    - Tính net worth (assets + accounts - debts)
    - _Yêu cầu: 14.1, 14.2, 14.4, 14.6_

- [ ] 3. Implement Savings Goals và Assets screens (Flutter)
  - [ ] 3.1 Implement SavingsGoalBloc, Repository và screens
    - Goal list với progress indicators, add/edit, contribute/withdraw screens
    - _Yêu cầu: 12.1, 12.2, 12.4, 12.7_
  - [ ] 3.2 Implement AssetBloc, Repository và screens
    - Asset list với total value, add/edit, net worth screen với breakdown chart
    - _Yêu cầu: 14.1, 14.3, 14.4, 14.5_
