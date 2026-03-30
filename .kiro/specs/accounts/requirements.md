# Yêu Cầu - Quản Lý Nguồn Tiền

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 2: Quản Lý Nguồn Tiền

**Câu chuyện người dùng:** Là người dùng, tôi muốn quản lý các nguồn tiền khác nhau, để biết số dư hiện tại của từng nguồn và theo dõi các thông tin đặc thù của từng loại nguồn tiền.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo, chỉnh sửa và xóa nguồn tiền.
2. Nguồn tiền SẼ bao gồm: tên, loại (ví, ngân hàng, tài khoản tiết kiệm, thẻ tín dụng), số dư ban đầu và biểu tượng.
3. KHI một giao dịch được tạo, THÌ API Backend SẼ tự động cập nhật số dư của nguồn tiền tương ứng.
4. Mobile App SẼ hiển thị tổng số dư của tất cả nguồn tiền.
5. KHI số dư nguồn tiền thay đổi, THÌ Mobile App SẼ cập nhật giao diện theo thời gian thực.
6. API Backend SẼ đảm bảo tính toàn vẹn dữ liệu khi cập nhật số dư đồng thời.
7. KHI người dùng tạo nguồn tiền loại tài khoản tiết kiệm, THÌ Mobile App SẼ cho phép nhập lãi suất, ngày rút dự kiến và số dư ban đầu.
8. KHI người dùng xem tài khoản tiết kiệm, THÌ API Backend SẼ tính toán và hiển thị tiền lãi dự kiến dựa trên lãi suất và thời gian đến ngày rút dự kiến.
9. API Backend SẼ tự động cập nhật tiền lãi dự kiến của tài khoản tiết kiệm mỗi ngày.
10. KHI người dùng tạo nguồn tiền loại thẻ tín dụng, THÌ Mobile App SẼ cho phép nhập hạn mức tín dụng, dư nợ hiện tại và ngày đến hạn thanh toán.
11. Mobile App SẼ hiển thị số tiền khả dụng của thẻ tín dụng (hạn mức − dư nợ hiện tại).
12. KHI ngày đến hạn thanh toán thẻ tín dụng còn 3 ngày, THÌ Notification Service SẼ gửi cảnh báo đến người dùng.
13. KHI đến ngày đến hạn thanh toán thẻ tín dụng, THÌ Notification Service SẼ gửi thông báo nhắc nhở thanh toán.
14. KHI người dùng thanh toán dư nợ thẻ tín dụng, THÌ API Backend SẼ cập nhật dư nợ hiện tại và số tiền khả dụng.
15. Người dùng SẼ xem danh sách giao dịch được lọc theo từng nguồn tiền.
16. KHI người dùng chọn một nguồn tiền, THÌ Mobile App SẼ hiển thị tổng thu và tổng chi của nguồn tiền đó.
17. Mobile App SẼ hiển thị thống kê chi tiết theo nguồn tiền bao gồm biểu đồ thu chi theo thời gian.
