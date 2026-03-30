# Thiết Kế - Báo Cáo, Đồng Bộ và Sao Lưu

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `reports`, `backups`)

## API Endpoints

```
POST   /api/reports/generate       - Tạo báo cáo PDF/Excel
GET    /api/reports                - Danh sách báo cáo
GET    /api/reports/{id}/download  - Tải xuống báo cáo

POST   /api/sync/pull              - Kéo dữ liệu mới nhất
POST   /api/sync/push              - Đẩy thay đổi local
GET    /api/sync/status            - Trạng thái đồng bộ

POST   /api/backup/create          - Tạo bản sao lưu
GET    /api/backup/list            - Danh sách bản sao lưu
POST   /api/backup/restore         - Khôi phục từ bản sao lưu
```

## Backend Components

**ReportService**:

- Generate PDF (iText/JasperReports) và Excel (Apache POI)
- Nội dung: tổng quan thu chi, chi tiết transactions, biểu đồ, phân tích theo category
- Lưu file lên S3, auto-delete sau 30 ngày

**SyncService**:

- `pull(userId, lastSyncAt)`: trả về tất cả records thay đổi sau `lastSyncAt`
- `push(userId, changes)`: nhận local changes, áp dụng conflict resolution
- Conflict resolution: last-write-wins dựa trên `updated_at` timestamp
- Batch sync cho hiệu năng

**BackupService**:

- Export toàn bộ data của user ra JSON, mã hóa, upload lên S3
- Restore: download, decrypt, import vào database
- Giữ tối đa 30 backups gần nhất (auto-delete cũ hơn)
- Scheduled daily backup nếu user bật auto backup

## Flutter Components

**Screens**:

- Report settings: chọn date range, nội dung; generate và download/share
- Sync status indicator trong app
- Backup settings: manual backup button, restore từ backup với confirmation dialog
- Offline mode: lưu changes vào SQLite, sync khi có mạng
