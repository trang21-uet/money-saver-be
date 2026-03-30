# Thiết Kế - Thống Kê và So Sánh Chi Tiêu

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `transactions`, `categories`)

## API Endpoints

```
GET    /api/statistics/overview    - Tổng quan thu chi (tổng thu, tổng chi, số dư ròng)
GET    /api/statistics/by-category - Thống kê theo danh mục với percentage
GET    /api/statistics/by-period   - Thống kê theo kỳ (day/week/month/year)
GET    /api/statistics/trends      - Xu hướng chi tiêu so với kỳ trước
GET    /api/statistics/compare     - So sánh giữa hai kỳ bất kỳ
```

**Query Parameters chung**: `start_date`, `end_date`, `period` (day/week/month/year)

## Backend Components

**StatisticsService**:

- Tính tổng thu, tổng chi, số dư ròng theo kỳ
- Thống kê theo category với percentage
- Xu hướng: so sánh với kỳ trước, tính % tăng/giảm
- Redis cache với TTL 5 phút (cache key: `stats:{userId}:{startDate}:{endDate}`)

**Luồng xử lý**:

1. Kiểm tra Redis cache
2. Cache miss → query PostgreSQL với index tối ưu
3. Tính toán, lưu vào cache
4. Trả về kết quả trong < 1 giây

## Flutter Components

**StatisticsBloc + StatisticsRepository**: Fetch statistics, cache locally

**Screens**:

- Statistics screen: overview card (tổng thu/chi/ròng), pie chart theo category, line chart xu hướng, period selector
- Comparison screen: bar chart so sánh hai kỳ, insights về thay đổi đáng kể
