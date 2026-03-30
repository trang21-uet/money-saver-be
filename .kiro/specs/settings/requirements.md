# Yêu Cầu - Cài Đặt và Thông Báo

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 19: Cài Đặt và Tùy Chỉnh

**Câu chuyện người dùng:** Là người dùng, tôi muốn tùy chỉnh giao diện và cài đặt ứng dụng, để phù hợp với sở thích cá nhân.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ chọn ngôn ngữ hiển thị (Tiếng Việt, English).
2. Người dùng SẼ chọn đơn vị tiền tệ (VND, USD, EUR và các loại khác).
3. Người dùng SẼ chọn giao diện (sáng, tối hoặc tự động theo hệ thống).
4. Người dùng SẼ chọn ngày bắt đầu tuần (Thứ Hai hoặc Chủ Nhật).
5. Người dùng SẼ chọn định dạng ngày tháng (DD/MM/YYYY hoặc MM/DD/YYYY).
6. Người dùng SẼ bật hoặc tắt thông báo cho từng loại sự kiện.
7. Mobile App SẼ lưu cài đặt cục bộ và đồng bộ lên API Backend.

## Yêu Cầu - Push Notifications

**Câu chuyện người dùng:** Là người dùng, tôi muốn nhận thông báo đẩy kịp thời, để không bỏ lỡ các sự kiện quan trọng.

### Tiêu Chí Chấp Nhận

1. API Backend SẼ gửi push notification qua Firebase Cloud Messaging.
2. Mobile App SẼ xử lý notification khi foreground, background và khi tap vào notification.
3. Notification SẼ được gửi cho các sự kiện: budget alert, debt reminder, payment reminder, group activity, savings goal completed.
4. Người dùng SẼ bật/tắt notification cho từng loại sự kiện trong Settings.
