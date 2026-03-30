# Yêu Cầu - Thống Kê và So Sánh Chi Tiêu

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 3: Thống Kê Chi Tiêu

**Câu chuyện người dùng:** Là người dùng, tôi muốn xem thống kê chi tiêu theo thời gian, để hiểu rõ thói quen tài chính của mình.

### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hiển thị biểu đồ chi tiêu theo ngày, tuần, tháng và năm.
2. Mobile App SẼ hiển thị phân tích chi tiêu theo danh mục.
3. KHI người dùng chọn khoảng thời gian, THÌ API Backend SẼ tính toán và trả về dữ liệu thống kê trong vòng 1 giây.
4. Mobile App SẼ hiển thị xu hướng chi tiêu so với các kỳ trước.
5. Mobile App SẼ hiển thị tỷ lệ phần trăm chi tiêu của từng danh mục.
6. API Backend SẼ tính toán tổng thu, tổng chi và số dư ròng cho kỳ được chọn.

## Yêu Cầu 13: So Sánh Chi Tiêu

**Câu chuyện người dùng:** Là người dùng, tôi muốn so sánh chi tiêu giữa các kỳ, để đánh giá hiệu quả quản lý tài chính.

### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hiển thị so sánh chi tiêu giữa tháng hiện tại và tháng trước.
2. Mobile App SẼ hiển thị so sánh chi tiêu theo từng danh mục giữa các kỳ.
3. API Backend SẼ tính toán phần trăm tăng hoặc giảm chi tiêu giữa các kỳ.
4. Mobile App SẼ hiển thị biểu đồ so sánh trực quan.
5. Người dùng SẼ chọn các kỳ bất kỳ để so sánh (tuần, tháng, quý, năm).
6. Mobile App SẼ hiển thị insight về các danh mục có sự thay đổi đáng kể.
