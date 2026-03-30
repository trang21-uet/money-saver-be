# Yêu Cầu - Quản Lý Giao Dịch

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 1: Quản Lý Giao Dịch Thu Chi

**Câu chuyện người dùng:** Là người dùng, tôi muốn ghi nhận và quản lý các giao dịch thu chi, để theo dõi dòng tiền của mình.

### Tiêu Chí Chấp Nhận

1. KHI người dùng tạo một giao dịch mới, THÌ Mobile App SẼ gửi dữ liệu lên API Backend.
2. KHI API Backend nhận yêu cầu tạo giao dịch, THÌ API Backend SẼ xác thực dữ liệu và lưu vào Database trong vòng 500ms.
3. Mobile App SẼ hiển thị danh sách giao dịch theo thứ tự thời gian giảm dần.
4. KHI người dùng chỉnh sửa một giao dịch, THÌ API Backend SẼ cập nhật dữ liệu và đồng bộ đến tất cả thiết bị của người dùng đó.
5. KHI người dùng xóa một giao dịch, THÌ API Backend SẼ thực hiện soft delete và giữ lại lịch sử.
6. Giao dịch SẼ bao gồm các thuộc tính: số tiền, loại (thu/chi), danh mục, nguồn tiền, ngày giờ, ghi chú và hình ảnh đính kèm.
7. KHI người dùng tìm kiếm giao dịch, THÌ Mobile App SẼ hỗ trợ lọc theo khoảng thời gian, danh mục, nguồn tiền và loại giao dịch.

## Yêu Cầu 16: Quản Lý Danh Mục

**Câu chuyện người dùng:** Là người dùng, tôi muốn tùy chỉnh danh mục chi tiêu, để phù hợp với nhu cầu cá nhân.

### Tiêu Chí Chấp Nhận

1. Hệ thống SẼ cung cấp danh sách danh mục mặc định (ăn uống, di chuyển, giải trí, mua sắm, hóa đơn, sức khỏe, giáo dục, khác).
2. Người dùng SẼ tạo danh mục tùy chỉnh với tên và biểu tượng.
3. Người dùng SẼ chỉnh sửa hoặc xóa danh mục tùy chỉnh.
4. KHI người dùng xóa danh mục đang được sử dụng, THÌ Mobile App SẼ yêu cầu chọn danh mục thay thế cho các giao dịch hiện có.
5. Người dùng SẼ sắp xếp thứ tự hiển thị của các danh mục.
6. API Backend SẼ đồng bộ danh mục tùy chỉnh giữa các thiết bị.

## Yêu Cầu 17: Tìm Kiếm và Lọc Nâng Cao

**Câu chuyện người dùng:** Là người dùng, tôi muốn tìm kiếm và lọc giao dịch nhanh chóng, để dễ dàng tra cứu thông tin.

### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hỗ trợ tìm kiếm giao dịch theo từ khóa trong phần ghi chú.
2. Mobile App SẼ hỗ trợ lọc giao dịch theo nhiều tiêu chí đồng thời (khoảng thời gian, danh mục, nguồn tiền, khoảng số tiền).
3. Mobile App SẼ lưu các bộ lọc thường dùng để truy cập nhanh.
4. API Backend SẼ trả về kết quả tìm kiếm trong vòng 500ms.
5. Mobile App SẼ hiển thị số lượng kết quả tìm thấy.
6. Mobile App SẼ hỗ trợ sắp xếp kết quả theo thời gian, số tiền hoặc danh mục.
