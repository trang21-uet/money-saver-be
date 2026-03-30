# Tasks - Thống Kê và So Sánh Chi Tiêu

- [ ] 1. Implement StatisticsService (Backend)
  - [ ] 1.1 Tạo StatisticsService với Redis caching
    - Tổng thu/chi/ròng theo kỳ, thống kê theo category với percentage
    - Xu hướng so với kỳ trước, TTL cache 5 phút
    - _Yêu cầu: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6_
  - [ ] 1.2 Tạo StatisticsController
    - GET /api/statistics/overview, by-category, by-period, trends, compare
    - Response time < 1 giây
    - _Yêu cầu: 3.3, 13.1, 13.2, 13.3_
  - [ ]\* 1.3 Viết tests cho statistics calculations
    - Test tính toán với nhiều transactions, cache hit/miss, performance với 1000+ transactions
    - _Yêu cầu: 3.3, 20.6_

- [ ] 2. Implement Statistics screens (Flutter)
  - [ ] 2.1 Implement StatisticsBloc và StatisticsRepository
    - _Yêu cầu: 3.3_
  - [ ] 2.2 Tạo statistics screen
    - Overview card, pie chart theo category, line chart xu hướng, period selector
    - _Yêu cầu: 3.1, 3.2, 3.4, 3.5_
  - [ ] 2.3 Tạo comparison screen
    - Bar chart so sánh hai kỳ, insights về thay đổi đáng kể
    - _Yêu cầu: 13.1, 13.2, 13.4, 13.6_
