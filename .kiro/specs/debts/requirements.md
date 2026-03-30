# Yêu Cầu - Quản Lý Nợ và Nhắc Nhở

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 5: Quản Lý Nợ và Cho Vay

**Câu chuyện người dùng:** Là người dùng, tôi muốn theo dõi các khoản nợ và cho vay, để quản lý các nghĩa vụ tài chính của mình.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo khoản nợ với các thông tin: người cho/vay, số tiền, ngày đáo hạn, lãi suất và trạng thái.
2. Mobile App SẼ phân biệt rõ ràng giữa khoản nợ phải trả và khoản cho vay.
3. KHI người dùng thanh toán một phần khoản nợ, THÌ API Backend SẼ cập nhật số tiền còn lại.
4. KHI khoản nợ đến hạn thanh toán, THÌ Notification Service SẼ gửi nhắc nhở trước 3 ngày, 1 ngày và vào đúng ngày đáo hạn.
5. Mobile App SẼ hiển thị tổng nợ phải trả và tổng tiền đang cho vay.
6. API Backend SẼ tính toán lãi suất tích lũy nếu khoản nợ có lãi suất.

## Yêu Cầu 6: Nhắc Nhở Thanh Toán

**Câu chuyện người dùng:** Là người dùng, tôi muốn nhận nhắc nhở về các khoản thanh toán định kỳ, để không bỏ lỡ các hóa đơn quan trọng.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo lời nhắc với các thông tin: tên, số tiền, tần suất (hàng ngày, hàng tuần, hàng tháng, hàng năm) và thời gian nhắc nhở.
2. KHI đến thời gian nhắc nhở, THÌ Notification Service SẼ gửi thông báo đến thiết bị của người dùng.
3. KHI người dùng hoàn thành thanh toán từ lời nhắc, THÌ Mobile App SẼ cho phép tạo giao dịch trực tiếp.
4. API Backend SẼ tự động tạo lời nhắc tiếp theo cho các khoản thanh toán định kỳ.
5. Người dùng SẼ tạm dừng hoặc xóa lời nhắc bất kỳ lúc nào.
6. Mobile App SẼ hiển thị danh sách các lời nhắc sắp tới trong 7 ngày.
