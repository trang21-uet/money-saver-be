# Tasks - Lập Kế Hoạch Chi Tiêu (Budget)

- [ ] 1. Implement BudgetService (Backend)
  - [ ] 1.1 Tạo BudgetRepository và BudgetService
    - CRUD operations, tính % sử dụng budget
    - _Yêu cầu: 4.1, 4.4, 4.5_
  - [ ] 1.2 Tạo BudgetController
    - GET/POST/PUT/DELETE /api/budgets, GET /api/budgets/{id}/progress
    - _Yêu cầu: 4.1, 4.6_
  - [ ] 1.3 Implement notification trigger cho budget alerts
    - Gửi notification khi vượt 80% và 100%
    - _Yêu cầu: 4.2, 4.3_

- [ ] 2. Implement Budget screens (Flutter)
  - [ ] 2.1 Implement BudgetBloc và BudgetRepository
    - _Yêu cầu: 4.1_
  - [ ] 2.2 Tạo budget list, add/edit, detail screens
    - Progress bars, spending breakdown
    - _Yêu cầu: 4.1, 4.4, 4.6_
