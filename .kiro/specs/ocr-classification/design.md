# Thiết Kế - OCR và Phân Loại Tự Động

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `ml_classification_models`)

## API Endpoints

```
POST   /api/ocr/scan               - Upload ảnh hóa đơn, trả về extracted data
GET    /api/ocr/history            - Lịch sử quét hóa đơn
```

## Backend Components

**OCRService**:

- Pre-process ảnh (resize, enhance contrast)
- Tích hợp Tesseract hoặc Google Vision API
- Extract: amount, date, merchant name, line items
- Trả về kết quả trong < 3 giây
- Lưu ảnh gốc lên S3, trả về `image_url`

**MLClassificationService**:

- Train Naive Bayes / Random Forest model từ lịch sử transactions của user
- `predict(note)` → `{categoryId, confidence}`
- Auto-apply nếu confidence > 90%
- Suggest nếu confidence 70-90%
- Lưu model per-user vào `ml_classification_models`
- Scheduled job retrain khi có 100 transactions mới

**Tích hợp với TransactionService**: Gọi MLClassificationService khi tạo transaction có note

## Flutter Components

**Screens**:

- Camera screen: chụp ảnh hóa đơn
- OCR review screen: hiển thị extracted data, cho phép chỉnh sửa trước khi tạo transaction
- Fallback: nhập thủ công nếu OCR thất bại
- Auto-classification suggestion: hiển thị gợi ý category khi confidence 70-90%
