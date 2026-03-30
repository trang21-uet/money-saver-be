# Yêu Cầu - Mục Tiêu Tiết Kiệm và Tài Sản

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 12: Mục Tiêu Tiết Kiệm

**Câu chuyện người dùng:** Là người dùng, tôi muốn đặt mục tiêu tiết kiệm, để có động lực tích lũy tài chính.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo saving goal với các thông tin: tên, số tiền mục tiêu, thời hạn và hình ảnh minh họa.
2. Người dùng SẼ đóng góp vào saving goal từ nguồn tiền.
3. KHI người dùng đóng góp vào saving goal, THÌ API Backend SẼ tạo giao dịch chuyển tiền tương ứng.
4. Mobile App SẼ hiển thị tiến độ đạt mục tiêu bằng thanh tiến trình và phần trăm hoàn thành.
5. API Backend SẼ tính toán số tiền cần tiết kiệm mỗi tháng để đạt mục tiêu đúng hạn.
6. KHI saving goal đạt 100%, THÌ Notification Service SẼ gửi thông báo chúc mừng đến người dùng.
7. Người dùng SẼ rút tiền từ saving goal khi cần thiết.

## Yêu Cầu 14: Quản Lý Tài Sản

**Câu chuyện người dùng:** Là người dùng, tôi muốn quản lý các tài sản có giá trị, để theo dõi tổng tài sản ròng của mình.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo tài sản với các thông tin: tên, loại (bất động sản, xe cộ, đầu tư, khác), giá trị hiện tại, ngày mua và ghi chú.
2. Người dùng SẼ cập nhật giá trị tài sản theo thời gian.
3. Mobile App SẼ hiển thị tổng giá trị của tất cả tài sản.
4. Mobile App SẼ tính toán và hiển thị tổng tài sản ròng (net worth = tài sản + nguồn tiền − khoản nợ).
5. Mobile App SẼ hiển thị biểu đồ phân bổ tài sản theo loại.
6. API Backend SẼ lưu trữ lịch sử thay đổi giá trị tài sản.
