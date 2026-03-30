# Yêu Cầu - Xác Thực và Bảo Mật

> Tham chiếu kiến trúc chung: `shared-architecture`

## Yêu Cầu 15: Xác Thực và Bảo Mật

**Câu chuyện người dùng:** Là người dùng, tôi muốn dữ liệu của mình được bảo mật, để đảm bảo thông tin tài chính luôn an toàn.

### Tiêu Chí Chấp Nhận

1. Người dùng SẼ đăng ký tài khoản bằng email và mật khẩu.
2. API Backend SẼ mã hóa mật khẩu bằng **bcrypt** trước khi lưu vào Database.
3. API Backend SẼ sử dụng **JWT token** để xác thực các API request.
4. **JWT token** SẼ có thời gian hết hạn là 7 ngày.
5. KHI người dùng bật xác thực sinh trắc học, THÌ Mobile App SẼ yêu cầu vân tay hoặc **Face ID** khi mở ứng dụng.
6. API Backend SẼ giới hạn 5 lần đăng nhập sai trong vòng 15 phút (rate limiting).
7. NẾU người dùng quên mật khẩu, THÌ API Backend SẼ gửi email đặt lại mật khẩu.
8. Database SẼ mã hóa các trường dữ liệu nhạy cảm như số tài khoản ngân hàng.
