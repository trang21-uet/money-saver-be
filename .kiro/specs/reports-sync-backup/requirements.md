# Yêu Cầu - Báo Cáo, Đồng Bộ và Sao Lưu

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 9: Xuất Báo Cáo Tài Chính

**Câu chuyện người dùng:** Là người dùng, tôi muốn xuất báo cáo tài chính, để lưu trữ hoặc chia sẻ với người khác.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ xuất báo cáo dưới định dạng PDF hoặc Excel.
2. Báo cáo SẼ bao gồm: tổng quan thu chi, chi tiết giao dịch, biểu đồ và phân tích theo danh mục.
3. KHI người dùng yêu cầu xuất báo cáo, THÌ API Backend SẼ tạo file trong vòng 5 giây.
4. Mobile App SẼ cho phép người dùng chọn khoảng thời gian và các nội dung cần đưa vào báo cáo.
5. API Backend SẼ lưu trữ các báo cáo đã tạo trong 30 ngày.
6. Người dùng SẼ tải xuống hoặc chia sẻ báo cáo qua email, tin nhắn hoặc ứng dụng khác.

## Yêu Cầu 10: Đồng Bộ Đa Thiết Bị

**Câu chuyện người dùng:** Là người dùng, tôi muốn dữ liệu được đồng bộ giữa các thiết bị, để truy cập mọi lúc mọi nơi.

### Tiêu Chí Chấp Nhận

1. KHI người dùng đăng nhập trên thiết bị mới, THÌ Sync Service SẼ tải xuống toàn bộ dữ liệu từ Database.
2. KHI người dùng tạo hoặc chỉnh sửa dữ liệu trên một thiết bị, THÌ Sync Service SẼ đồng bộ đến tất cả thiết bị còn lại trong vòng 2 giây.
3. TRONG KHI thiết bị không có kết nối mạng, THÌ Mobile App SẼ lưu trữ các thay đổi cục bộ (offline mode).
4. KHI thiết bị kết nối lại mạng, THÌ Sync Service SẼ đồng bộ các thay đổi cục bộ lên Database.
5. NẾU xảy ra xung đột dữ liệu, THÌ Sync Service SẼ ưu tiên thay đổi mới nhất dựa trên timestamp.
6. Sync Service SẼ mã hóa toàn bộ dữ liệu trong quá trình truyền tải.

## Yêu Cầu 18: Sao Lưu và Khôi Phục Dữ Liệu

**Câu chuyện người dùng:** Là người dùng, tôi muốn sao lưu và khôi phục dữ liệu, để bảo vệ thông tin khỏi mất mát.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo bản sao lưu thủ công bất kỳ lúc nào.
2. KHI người dùng bật sao lưu tự động, THÌ API Backend SẼ tạo bản sao lưu hàng ngày.
3. API Backend SẼ lưu trữ bản sao lưu trên cloud storage.
4. Người dùng SẼ khôi phục dữ liệu từ bất kỳ bản sao lưu nào.
5. KHI khôi phục dữ liệu, THÌ Mobile App SẼ cảnh báo rằng dữ liệu hiện tại sẽ bị ghi đè.
6. API Backend SẼ mã hóa bản sao lưu trước khi lưu trữ.
7. API Backend SẼ giữ tối đa 30 bản sao lưu gần nhất.
