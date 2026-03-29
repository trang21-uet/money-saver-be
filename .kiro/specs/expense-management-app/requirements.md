# Tài Liệu Yêu Cầu

## Giới Thiệu

Ứng dụng **Banana** là một hệ thống toàn diện giúp người dùng theo dõi, phân tích và lập kế hoạch tài chính cá nhân. Hệ thống bao gồm ứng dụng di động **Flutter**, backend **Spring Boot** và cơ sở dữ liệu **PostgreSQL**, cung cấp các tính năng từ cơ bản đến nâng cao như quản lý giao dịch, nguồn tiền, thống kê, lập kế hoạch, quản lý nợ, nhắc nhở thanh toán, phân loại tự động, chia sẻ nhóm, xuất báo cáo, đồng bộ đa thiết bị, quét hóa đơn **OCR**, mục tiêu tiết kiệm, so sánh chi tiêu và quản lý tài sản.

## Thuật Ngữ

- **Hệ thống**: Toàn bộ ứng dụng **Banana** bao gồm frontend, backend và database
- **Người dùng**: Người dùng cuối sử dụng ứng dụng di động
- **Giao dịch**: Giao dịch tài chính (thu hoặc chi)
- **Nguồn tiền**: Các nguồn tiền như ví, tài khoản ngân hàng, thẻ tín dụng
- **Danh mục**: Danh mục dùng để phân loại giao dịch
- **Ngân sách (Budget)**: Kế hoạch chi tiêu với giới hạn ngân sách
- **Khoản nợ**: Khoản nợ cần trả hoặc khoản cho vay
- **Lời nhắc**: Nhắc nhở thanh toán
- **Nhóm**: Nhóm người dùng chia sẻ chi tiêu chung
- **Báo cáo**: Báo cáo tài chính
- **Hóa đơn**: Hóa đơn được quét bằng **OCR**
- **Mục tiêu tiết kiệm**: Mục tiêu tích lũy tài chính
- **Tài sản**: Tài sản có giá trị của người dùng
- **API Backend**: **API Spring Boot** xử lý logic nghiệp vụ
- **Database**: Cơ sở dữ liệu **PostgreSQL** lưu trữ dữ liệu
- **Mobile App**: Ứng dụng **Flutter** trên thiết bị di động
- **Sync Service**: Dịch vụ đồng bộ dữ liệu giữa các thiết bị
- **OCR Service**: Dịch vụ nhận dạng ký tự quang học
- **Notification Service**: Dịch vụ gửi thông báo đẩy
- **Auto Classifier**: Bộ phân loại giao dịch tự động

## Các Yêu Cầu

### Yêu Cầu 1: Quản Lý Giao Dịch Thu Chi

**Câu chuyện người dùng:** Là người dùng, tôi muốn ghi nhận và quản lý các giao dịch thu chi, để theo dõi dòng tiền của mình.

#### Tiêu Chí Chấp Nhận

1. KHI người dùng tạo một giao dịch mới, THÌ Mobile App SẼ gửi dữ liệu lên API Backend.
2. KHI API Backend nhận yêu cầu tạo giao dịch, THÌ API Backend SẼ xác thực dữ liệu và lưu vào Database trong vòng 500ms.
3. Mobile App SẼ hiển thị danh sách giao dịch theo thứ tự thời gian giảm dần.
4. KHI người dùng chỉnh sửa một giao dịch, THÌ API Backend SẼ cập nhật dữ liệu và đồng bộ đến tất cả thiết bị của người dùng đó.
5. KHI người dùng xóa một giao dịch, THÌ API Backend SẼ thực hiện soft delete và giữ lại lịch sử.
6. Giao dịch SẼ bao gồm các thuộc tính: số tiền, loại (thu/chi), danh mục, nguồn tiền, ngày giờ, ghi chú và hình ảnh đính kèm.
7. KHI người dùng tìm kiếm giao dịch, THÌ Mobile App SẼ hỗ trợ lọc theo khoảng thời gian, danh mục, nguồn tiền và loại giao dịch.

---

### Yêu Cầu 2: Quản Lý Nguồn Tiền

**Câu chuyện người dùng:** Là người dùng, tôi muốn quản lý các nguồn tiền khác nhau, để biết số dư hiện tại của từng nguồn và theo dõi các thông tin đặc thù của từng loại nguồn tiền.

#### Tiêu Chí Chấp Nhận

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

---

### Yêu Cầu 3: Thống Kê Chi Tiêu

**Câu chuyện người dùng:** Là người dùng, tôi muốn xem thống kê chi tiêu theo thời gian, để hiểu rõ thói quen tài chính của mình.

#### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hiển thị biểu đồ chi tiêu theo ngày, tuần, tháng và năm.
2. Mobile App SẼ hiển thị phân tích chi tiêu theo danh mục.
3. KHI người dùng chọn khoảng thời gian, THÌ API Backend SẼ tính toán và trả về dữ liệu thống kê trong vòng 1 giây.
4. Mobile App SẼ hiển thị xu hướng chi tiêu so với các kỳ trước.
5. Mobile App SẼ hiển thị tỷ lệ phần trăm chi tiêu của từng danh mục.
6. API Backend SẼ tính toán tổng thu, tổng chi và số dư ròng cho kỳ được chọn.

---

### Yêu Cầu 4: Lập Kế Hoạch Chi Tiêu (Budget)

**Câu chuyện người dùng:** Là người dùng, tôi muốn lập kế hoạch chi tiêu với ngân sách giới hạn, để kiểm soát chi tiêu của mình.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo budget với giới hạn ngân sách cho từng danh mục và khoảng thời gian.
2. KHI chi tiêu vượt quá 80% ngân sách, THÌ Notification Service SẼ gửi cảnh báo đến người dùng.
3. KHI chi tiêu vượt quá 100% ngân sách, THÌ Notification Service SẼ gửi thông báo khẩn cấp.
4. Mobile App SẼ hiển thị tiến độ chi tiêu so với budget bằng thanh tiến trình.
5. API Backend SẼ tính toán phần trăm sử dụng budget sau mỗi giao dịch mới.
6. Người dùng SẼ chỉnh sửa hoặc xóa budget bất kỳ lúc nào.

---

### Yêu Cầu 5: Quản Lý Nợ và Cho Vay

**Câu chuyện người dùng:** Là người dùng, tôi muốn theo dõi các khoản nợ và cho vay, để quản lý các nghĩa vụ tài chính của mình.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo khoản nợ với các thông tin: người cho/vay, số tiền, ngày đáo hạn, lãi suất và trạng thái.
2. Mobile App SẼ phân biệt rõ ràng giữa khoản nợ phải trả và khoản cho vay.
3. KHI người dùng thanh toán một phần khoản nợ, THÌ API Backend SẼ cập nhật số tiền còn lại.
4. KHI khoản nợ đến hạn thanh toán, THÌ Notification Service SẼ gửi nhắc nhở trước 3 ngày, 1 ngày và vào đúng ngày đáo hạn.
5. Mobile App SẼ hiển thị tổng nợ phải trả và tổng tiền đang cho vay.
6. API Backend SẼ tính toán lãi suất tích lũy nếu khoản nợ có lãi suất.

---

### Yêu Cầu 6: Nhắc Nhở Thanh Toán

**Câu chuyện người dùng:** Là người dùng, tôi muốn nhận nhắc nhở về các khoản thanh toán định kỳ, để không bỏ lỡ các hóa đơn quan trọng.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo lời nhắc với các thông tin: tên, số tiền, tần suất (hàng ngày, hàng tuần, hàng tháng, hàng năm) và thời gian nhắc nhở.
2. KHI đến thời gian nhắc nhở, THÌ Notification Service SẼ gửi thông báo đến thiết bị của người dùng.
3. KHI người dùng hoàn thành thanh toán từ lời nhắc, THÌ Mobile App SẼ cho phép tạo giao dịch trực tiếp.
4. API Backend SẼ tự động tạo lời nhắc tiếp theo cho các khoản thanh toán định kỳ.
5. Người dùng SẼ tạm dừng hoặc xóa lời nhắc bất kỳ lúc nào.
6. Mobile App SẼ hiển thị danh sách các lời nhắc sắp tới trong 7 ngày.

---

### Yêu Cầu 7: Phân Loại Chi Tiêu Tự Động (Auto Classify)

**Câu chuyện người dùng:** Là người dùng, tôi muốn hệ thống tự động phân loại giao dịch, để tiết kiệm thời gian nhập liệu.

#### Tiêu Chí Chấp Nhận

1. KHI người dùng tạo giao dịch có ghi chú, THÌ Auto Classifier SẼ đề xuất danh mục dựa trên lịch sử giao dịch.
2. Auto Classifier SẼ học từ các lựa chọn danh mục của người dùng để cải thiện độ chính xác theo thời gian.
3. KHI độ tin cậy của đề xuất trên 90%, THÌ Auto Classifier SẼ tự động áp dụng danh mục mà không cần xác nhận.
4. KHI độ tin cậy của đề xuất từ 70% đến 90%, THÌ Mobile App SẼ hiển thị gợi ý để người dùng xác nhận.
5. API Backend SẼ lưu trữ mô hình phân loại riêng biệt cho từng người dùng.
6. Người dùng SẼ có thể tắt tính năng auto classify trong phần cài đặt.

---

### Yêu Cầu 8: Chia Sẻ Chi Tiêu Nhóm

**Câu chuyện người dùng:** Là người dùng, tôi muốn chia sẻ và quản lý chi tiêu chung với nhóm, để theo dõi chi phí tập thể.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo nhóm và mời thành viên khác qua email hoặc mã mời.
2. KHI thành viên chấp nhận lời mời, THÌ API Backend SẼ thêm người dùng vào nhóm.
3. Người dùng SẼ tạo giao dịch thuộc nhóm và chỉ định người đã thanh toán.
4. API Backend SẼ tính toán số tiền mỗi thành viên cần thanh toán dựa trên tỷ lệ chia sẻ.
5. Mobile App SẼ hiển thị tổng chi tiêu của nhóm và số dư của từng thành viên.
6. Người dùng SẼ đánh dấu đã thanh toán cho thành viên khác trong nhóm.
7. KHI có giao dịch mới trong nhóm, THÌ Notification Service SẼ thông báo đến tất cả thành viên.
8. Người dùng SẼ rời khỏi nhóm sau khi đã thanh toán hết các khoản nợ trong nhóm.

---

### Yêu Cầu 9: Xuất Báo Cáo Tài Chính

**Câu chuyện người dùng:** Là người dùng, tôi muốn xuất báo cáo tài chính, để lưu trữ hoặc chia sẻ với người khác.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ xuất báo cáo dưới định dạng **PDF** hoặc **Excel**.
2. Báo cáo SẼ bao gồm: tổng quan thu chi, chi tiết giao dịch, biểu đồ và phân tích theo danh mục.
3. KHI người dùng yêu cầu xuất báo cáo, THÌ API Backend SẼ tạo file trong vòng 5 giây.
4. Mobile App SẼ cho phép người dùng chọn khoảng thời gian và các nội dung cần đưa vào báo cáo.
5. API Backend SẼ lưu trữ các báo cáo đã tạo trong 30 ngày.
6. Người dùng SẼ tải xuống hoặc chia sẻ báo cáo qua email, tin nhắn hoặc ứng dụng khác.

---

### Yêu Cầu 10: Đồng Bộ Đa Thiết Bị

**Câu chuyện người dùng:** Là người dùng, tôi muốn dữ liệu được đồng bộ giữa các thiết bị, để truy cập mọi lúc mọi nơi.

#### Tiêu Chí Chấp Nhận

1. KHI người dùng đăng nhập trên thiết bị mới, THÌ Sync Service SẼ tải xuống toàn bộ dữ liệu từ Database.
2. KHI người dùng tạo hoặc chỉnh sửa dữ liệu trên một thiết bị, THÌ Sync Service SẼ đồng bộ đến tất cả thiết bị còn lại trong vòng 2 giây.
3. TRONG KHI thiết bị không có kết nối mạng, THÌ Mobile App SẼ lưu trữ các thay đổi cục bộ (offline mode).
4. KHI thiết bị kết nối lại mạng, THÌ Sync Service SẼ đồng bộ các thay đổi cục bộ lên Database.
5. NẾU xảy ra xung đột dữ liệu, THÌ Sync Service SẼ ưu tiên thay đổi mới nhất dựa trên timestamp.
6. Sync Service SẼ mã hóa toàn bộ dữ liệu trong quá trình truyền tải.

---

### Yêu Cầu 11: Quét Hóa Đơn (OCR)

**Câu chuyện người dùng:** Là người dùng, tôi muốn quét hóa đơn để tự động tạo giao dịch, để giảm thiểu nhập liệu thủ công.

#### Tiêu Chí Chấp Nhận

1. KHI người dùng chụp ảnh hóa đơn, THÌ Mobile App SẼ gửi ảnh đến OCR Service.
2. OCR Service SẼ trích xuất các thông tin: số tiền, ngày giờ, tên cửa hàng và các mục chi tiêu.
3. KHI OCR Service hoàn tất xử lý, THÌ Mobile App SẼ hiển thị thông tin đã trích xuất để người dùng xác nhận.
4. OCR Service SẼ xử lý ảnh và trả về kết quả trong vòng 3 giây.
5. Người dùng SẼ chỉnh sửa thông tin trích xuất trước khi tạo giao dịch.
6. Mobile App SẼ lưu ảnh hóa đơn gốc đính kèm với giao dịch.
7. NẾU OCR Service không trích xuất được thông tin, THÌ Mobile App SẼ cho phép người dùng nhập thủ công.

---

### Yêu Cầu 12: Mục Tiêu Tiết Kiệm (Saving Goal)

**Câu chuyện người dùng:** Là người dùng, tôi muốn đặt mục tiêu tiết kiệm, để có động lực tích lũy tài chính.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo saving goal với các thông tin: tên, số tiền mục tiêu, thời hạn và hình ảnh minh họa.
2. Người dùng SẼ đóng góp vào saving goal từ nguồn tiền.
3. KHI người dùng đóng góp vào saving goal, THÌ API Backend SẼ tạo giao dịch chuyển tiền tương ứng.
4. Mobile App SẼ hiển thị tiến độ đạt mục tiêu bằng thanh tiến trình và phần trăm hoàn thành.
5. API Backend SẼ tính toán số tiền cần tiết kiệm mỗi tháng để đạt mục tiêu đúng hạn.
6. KHI saving goal đạt 100%, THÌ Notification Service SẼ gửi thông báo chúc mừng đến người dùng.
7. Người dùng SẼ rút tiền từ saving goal khi cần thiết.

---

### Yêu Cầu 13: So Sánh Chi Tiêu

**Câu chuyện người dùng:** Là người dùng, tôi muốn so sánh chi tiêu giữa các kỳ, để đánh giá hiệu quả quản lý tài chính.

#### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hiển thị so sánh chi tiêu giữa tháng hiện tại và tháng trước.
2. Mobile App SẼ hiển thị so sánh chi tiêu theo từng danh mục giữa các kỳ.
3. API Backend SẼ tính toán phần trăm tăng hoặc giảm chi tiêu giữa các kỳ.
4. Mobile App SẼ hiển thị biểu đồ so sánh trực quan.
5. Người dùng SẼ chọn các kỳ bất kỳ để so sánh (tuần, tháng, quý, năm).
6. Mobile App SẼ hiển thị insight về các danh mục có sự thay đổi đáng kể.

---

### Yêu Cầu 14: Quản Lý Tài Sản

**Câu chuyện người dùng:** Là người dùng, tôi muốn quản lý các tài sản có giá trị, để theo dõi tổng tài sản ròng của mình.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo tài sản với các thông tin: tên, loại (bất động sản, xe cộ, đầu tư, khác), giá trị hiện tại, ngày mua và ghi chú.
2. Người dùng SẼ cập nhật giá trị tài sản theo thời gian.
3. Mobile App SẼ hiển thị tổng giá trị của tất cả tài sản.
4. Mobile App SẼ tính toán và hiển thị tổng tài sản ròng (net worth = tài sản + nguồn tiền − khoản nợ).
5. Mobile App SẼ hiển thị biểu đồ phân bổ tài sản theo loại.
6. API Backend SẼ lưu trữ lịch sử thay đổi giá trị tài sản.

---

### Yêu Cầu 15: Xác Thực và Bảo Mật

**Câu chuyện người dùng:** Là người dùng, tôi muốn dữ liệu của mình được bảo mật, để đảm bảo thông tin tài chính luôn an toàn.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ đăng ký tài khoản bằng email và mật khẩu.
2. API Backend SẼ mã hóa mật khẩu bằng **bcrypt** trước khi lưu vào Database.
3. API Backend SẼ sử dụng **JWT token** để xác thực các API request.
4. **JWT token** SẼ có thời gian hết hạn là 7 ngày.
5. KHI người dùng bật xác thực sinh trắc học, THÌ Mobile App SẼ yêu cầu vân tay hoặc **Face ID** khi mở ứng dụng.
6. API Backend SẼ giới hạn 5 lần đăng nhập sai trong vòng 15 phút (rate limiting).
7. NẾU người dùng quên mật khẩu, THÌ API Backend SẼ gửi email đặt lại mật khẩu.
8. Database SẼ mã hóa các trường dữ liệu nhạy cảm như số tài khoản ngân hàng.

---

### Yêu Cầu 16: Quản Lý Danh Mục

**Câu chuyện người dùng:** Là người dùng, tôi muốn tùy chỉnh danh mục chi tiêu, để phù hợp với nhu cầu cá nhân.

#### Tiêu Chí Chấp Nhận

1. Hệ thống SẼ cung cấp danh sách danh mục mặc định (ăn uống, di chuyển, giải trí, mua sắm, hóa đơn, sức khỏe, giáo dục, khác).
2. Người dùng SẼ tạo danh mục tùy chỉnh với tên và biểu tượng.
3. Người dùng SẼ chỉnh sửa hoặc xóa danh mục tùy chỉnh.
4. KHI người dùng xóa danh mục đang được sử dụng, THÌ Mobile App SẼ yêu cầu chọn danh mục thay thế cho các giao dịch hiện có.
5. Người dùng SẼ sắp xếp thứ tự hiển thị của các danh mục.
6. API Backend SẼ đồng bộ danh mục tùy chỉnh giữa các thiết bị.

---

### Yêu Cầu 17: Tìm Kiếm và Lọc Nâng Cao

**Câu chuyện người dùng:** Là người dùng, tôi muốn tìm kiếm và lọc giao dịch nhanh chóng, để dễ dàng tra cứu thông tin.

#### Tiêu Chí Chấp Nhận

1. Mobile App SẼ hỗ trợ tìm kiếm giao dịch theo từ khóa trong phần ghi chú.
2. Mobile App SẼ hỗ trợ lọc giao dịch theo nhiều tiêu chí đồng thời (khoảng thời gian, danh mục, nguồn tiền, khoảng số tiền).
3. Mobile App SẼ lưu các bộ lọc thường dùng để truy cập nhanh.
4. API Backend SẼ trả về kết quả tìm kiếm trong vòng 500ms.
5. Mobile App SẼ hiển thị số lượng kết quả tìm thấy.
6. Mobile App SẼ hỗ trợ sắp xếp kết quả theo thời gian, số tiền hoặc danh mục.

---

### Yêu Cầu 18: Sao Lưu và Khôi Phục Dữ Liệu

**Câu chuyện người dùng:** Là người dùng, tôi muốn sao lưu và khôi phục dữ liệu, để bảo vệ thông tin khỏi mất mát.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ tạo bản sao lưu thủ công bất kỳ lúc nào.
2. KHI người dùng bật sao lưu tự động, THÌ API Backend SẼ tạo bản sao lưu hàng ngày.
3. API Backend SẼ lưu trữ bản sao lưu trên **cloud storage**.
4. Người dùng SẼ khôi phục dữ liệu từ bất kỳ bản sao lưu nào.
5. KHI khôi phục dữ liệu, THÌ Mobile App SẼ cảnh báo rằng dữ liệu hiện tại sẽ bị ghi đè.
6. API Backend SẼ mã hóa bản sao lưu trước khi lưu trữ.
7. API Backend SẼ giữ tối đa 30 bản sao lưu gần nhất.

---

### Yêu Cầu 19: Cài Đặt và Tùy Chỉnh

**Câu chuyện người dùng:** Là người dùng, tôi muốn tùy chỉnh giao diện và cài đặt ứng dụng, để phù hợp với sở thích cá nhân.

#### Tiêu Chí Chấp Nhận

1. Người dùng SẼ chọn ngôn ngữ hiển thị (Tiếng Việt, English).
2. Người dùng SẼ chọn đơn vị tiền tệ (VND, USD, EUR và các loại khác).
3. Người dùng SẼ chọn giao diện (sáng, tối hoặc tự động theo hệ thống).
4. Người dùng SẼ chọn ngày bắt đầu tuần (Thứ Hai hoặc Chủ Nhật).
5. Người dùng SẼ chọn định dạng ngày tháng (DD/MM/YYYY hoặc MM/DD/YYYY).
6. Người dùng SẼ bật hoặc tắt thông báo cho từng loại sự kiện.
7. Mobile App SẼ lưu cài đặt cục bộ và đồng bộ lên API Backend.

---

### Yêu Cầu 20: Hiệu Năng và Khả Năng Mở Rộng

**Câu chuyện người dùng:** Là người dùng, tôi muốn ứng dụng hoạt động mượt mà, để có trải nghiệm tốt nhất.

#### Tiêu Chí Chấp Nhận

1. Mobile App SẼ khởi động trong vòng 2 giây.
2. Mobile App SẼ hiển thị danh sách 100 giao dịch trong vòng 500ms.
3. Mobile App SẼ hỗ trợ **lazy loading** khi cuộn danh sách dài.
4. API Backend SẼ xử lý tối thiểu 100 request đồng thời.
5. Database SẼ được tối ưu hóa với **index** cho các truy vấn thường dùng.
6. API Backend SẼ sử dụng **caching** cho dữ liệu thống kê.
7. Mobile App SẼ hoạt động mượt mà với ít nhất 10.000 giao dịch trong Database.
