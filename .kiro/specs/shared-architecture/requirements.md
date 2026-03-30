# Yêu Cầu - Kiến Trúc Chung

## Giới Thiệu

Tài liệu này mô tả các yêu cầu chung về kiến trúc, database schema và API contracts áp dụng cho toàn bộ hệ thống Banana. Đây là tài liệu tham chiếu cho tất cả các spec tính năng khác.

## Thuật Ngữ

- **Hệ thống**: Toàn bộ ứng dụng Banana bao gồm frontend Flutter, backend Spring Boot và database PostgreSQL
- **API Backend**: API Spring Boot xử lý logic nghiệp vụ
- **Database**: Cơ sở dữ liệu PostgreSQL lưu trữ dữ liệu
- **Mobile App**: Ứng dụng Flutter trên thiết bị di động
- **Soft Delete**: Xóa mềm bằng cột `deleted_at` thay vì xóa vật lý

## Các Yêu Cầu

### Yêu Cầu 1: Kiến Trúc Ba Tầng

**Câu chuyện người dùng:** Là developer, tôi muốn hệ thống có kiến trúc rõ ràng, để dễ bảo trì và mở rộng.

#### Tiêu Chí Chấp Nhận

1. Hệ thống SẼ sử dụng kiến trúc Client-Server ba tầng: Mobile App, API Backend, Database.
2. API Backend SẼ sử dụng RESTful API với JSON format.
3. Database SẼ sử dụng PostgreSQL làm cơ sở dữ liệu quan hệ chính.
4. API Backend SẼ sử dụng Redis cho caching dữ liệu thống kê.
5. Mobile App SẼ sử dụng SQLite cho local storage (offline-first).

### Yêu Cầu 2: Chuẩn Hóa Database

**Câu chuyện người dùng:** Là developer, tôi muốn database có chuẩn thiết kế nhất quán, để đảm bảo tính toàn vẹn dữ liệu.

#### Tiêu Chí Chấp Nhận

1. Tất cả bảng SẼ có cột `id BIGSERIAL PRIMARY KEY`.
2. Tất cả bảng SẼ có cột `created_at`, `updated_at` kiểu TIMESTAMP.
3. Các bảng hỗ trợ soft delete SẼ có cột `deleted_at TIMESTAMP`.
4. Các bảng hỗ trợ conflict resolution SẼ có cột `version INTEGER`.
5. Số tiền SẼ được lưu kiểu `DECIMAL(15, 2)` để tránh lỗi làm tròn.
6. Database SẼ có index trên các cột thường xuyên query (user_id, date, type, deleted_at).

### Yêu Cầu 3: Chuẩn Hóa API

**Câu chuyện người dùng:** Là developer, tôi muốn API có chuẩn response nhất quán, để dễ tích hợp từ Mobile App.

#### Tiêu Chí Chấp Nhận

1. Tất cả API SẼ trả về JSON với cấu trúc nhất quán.
2. API SẼ sử dụng HTTP status codes chuẩn (200, 201, 400, 401, 403, 404, 500).
3. API SẼ hỗ trợ pagination với các tham số `page`, `size`, `sort`.
4. API SẼ trả về error response với `status`, `message`, `timestamp`.
5. Tất cả API (trừ auth) SẼ yêu cầu JWT token trong header `Authorization: Bearer {token}`.

### Yêu Cầu 4: Hiệu Năng và Khả Năng Mở Rộng

**Câu chuyện người dùng:** Là người dùng, tôi muốn ứng dụng hoạt động mượt mà, để có trải nghiệm tốt nhất.

#### Tiêu Chí Chấp Nhận

1. Mobile App SẼ khởi động trong vòng 2 giây.
2. Mobile App SẼ hiển thị danh sách 100 giao dịch trong vòng 500ms.
3. Mobile App SẼ hỗ trợ lazy loading khi cuộn danh sách dài.
4. API Backend SẼ xử lý tối thiểu 100 request đồng thời.
5. Database SẼ được tối ưu hóa với index cho các truy vấn thường dùng.
6. API Backend SẼ sử dụng caching cho dữ liệu thống kê.
7. Mobile App SẼ hoạt động mượt mà với ít nhất 10.000 giao dịch trong Database.
