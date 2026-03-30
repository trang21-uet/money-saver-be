# Yêu Cầu - Lập Kế Hoạch Chi Tiêu (Budget)

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 4: Lập Kế Hoạch Chi Tiêu

**Câu chuyện người dùng:** Là người dùng, tôi muốn lập kế hoạch chi tiêu với ngân sách giới hạn, để kiểm soát chi tiêu của mình.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo budget với giới hạn ngân sách cho từng danh mục và khoảng thời gian.
2. KHI chi tiêu vượt quá 80% ngân sách, THÌ Notification Service SẼ gửi cảnh báo đến người dùng.
3. KHI chi tiêu vượt quá 100% ngân sách, THÌ Notification Service SẼ gửi thông báo khẩn cấp.
4. Mobile App SẼ hiển thị tiến độ chi tiêu so với budget bằng thanh tiến trình.
5. API Backend SẼ tính toán phần trăm sử dụng budget sau mỗi giao dịch mới.
6. Người dùng SẼ chỉnh sửa hoặc xóa budget bất kỳ lúc nào.
