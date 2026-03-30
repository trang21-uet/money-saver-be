# Yêu Cầu - Chia Sẻ Chi Tiêu Nhóm

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 8: Chia Sẻ Chi Tiêu Nhóm

**Câu chuyện người dùng:** Là người dùng, tôi muốn chia sẻ và quản lý chi tiêu chung với nhóm, để theo dõi chi phí tập thể.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo nhóm và mời thành viên khác qua email hoặc mã mời.
2. KHI thành viên chấp nhận lời mời, THÌ API Backend SẼ thêm người dùng vào nhóm.
3. Người dùng SẼ tạo giao dịch thuộc nhóm và chỉ định người đã thanh toán.
4. API Backend SẼ tính toán số tiền mỗi thành viên cần thanh toán dựa trên tỷ lệ chia sẻ.
5. Mobile App SẼ hiển thị tổng chi tiêu của nhóm và số dư của từng thành viên.
6. Người dùng SẼ đánh dấu đã thanh toán cho thành viên khác trong nhóm.
7. KHI có giao dịch mới trong nhóm, THÌ Notification Service SẼ thông báo đến tất cả thành viên.
8. Người dùng SẼ rời khỏi nhóm sau khi đã thanh toán hết các khoản nợ trong nhóm.
