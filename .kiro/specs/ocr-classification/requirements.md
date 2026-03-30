# Yêu Cầu - OCR và Phân Loại Tự Động

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 7: Phân Loại Chi Tiêu Tự Động

**Câu chuyện người dùng:** Là người dùng, tôi muốn hệ thống tự động phân loại giao dịch, để tiết kiệm thời gian nhập liệu.

### Tiêu Chí Chấp Nhận

1. KHI người dùng tạo giao dịch có ghi chú, THÌ Auto Classifier SẼ đề xuất danh mục dựa trên lịch sử giao dịch.
2. Auto Classifier SẼ học từ các lựa chọn danh mục của người dùng để cải thiện độ chính xác theo thời gian.
3. KHI độ tin cậy của đề xuất trên 90%, THÌ Auto Classifier SẼ tự động áp dụng danh mục mà không cần xác nhận.
4. KHI độ tin cậy của đề xuất từ 70% đến 90%, THÌ Mobile App SẼ hiển thị gợi ý để người dùng xác nhận.
5. API Backend SẼ lưu trữ mô hình phân loại riêng biệt cho từng người dùng.
6. Người dùng SẼ có thể tắt tính năng auto classify trong phần cài đặt.

## Yêu Cầu 11: Quét Hóa Đơn (OCR)

**Câu chuyện người dùng:** Là người dùng, tôi muốn quét hóa đơn để tự động tạo giao dịch, để giảm thiểu nhập liệu thủ công.

### Tiêu Chí Chấp Nhận

1. KHI người dùng chụp ảnh hóa đơn, THÌ Mobile App SẼ gửi ảnh đến OCR Service.
2. OCR Service SẼ trích xuất các thông tin: số tiền, ngày giờ, tên cửa hàng và các mục chi tiêu.
3. KHI OCR Service hoàn tất xử lý, THÌ Mobile App SẼ hiển thị thông tin đã trích xuất để người dùng xác nhận.
4. OCR Service SẼ xử lý ảnh và trả về kết quả trong vòng 3 giây.
5. Người dùng SẼ chỉnh sửa thông tin trích xuất trước khi tạo giao dịch.
6. Mobile App SẼ lưu ảnh hóa đơn gốc đính kèm với giao dịch.
7. NẾU OCR Service không trích xuất được thông tin, THÌ Mobile App SẼ cho phép người dùng nhập thủ công.
