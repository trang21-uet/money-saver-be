# Tasks - OCR và Phân Loại Tự Động

- [ ] 1. Implement ML Classification (Backend)
  - [ ] 1.1 Tạo MLClassificationService
    - Train Naive Bayes model từ lịch sử transactions, predict với confidence score
    - Lưu model per-user, scheduled retrain mỗi 100 transactions mới
    - _Yêu cầu: 7.1, 7.2, 7.5_
  - [ ] 1.2 Tích hợp auto classification vào TransactionService
    - Auto-apply nếu confidence > 90%, suggest nếu 70-90%
    - _Yêu cầu: 7.1, 7.3, 7.4_
  - [ ]\* 1.3 Viết tests cho ML classification
    - Test training, prediction accuracy, confidence thresholds
    - _Yêu cầu: 7.1, 7.3, 7.4_

- [ ] 2. Implement OCR Service (Backend)
  - [ ] 2.1 Tạo OCRService với Tesseract/Google Vision API
    - Pre-process ảnh, extract amount/date/merchant, lưu ảnh lên S3
    - _Yêu cầu: 11.1, 11.2, 11.4, 11.6_
  - [ ] 2.2 Tạo OCRController
    - POST /api/ocr/scan, GET /api/ocr/history
    - _Yêu cầu: 11.1, 11.3_
  - [ ]\* 2.3 Viết tests cho OCR extraction
    - Test với sample receipt images, error handling khi OCR fails
    - _Yêu cầu: 11.4, 11.7_

- [ ] 3. Implement OCR scanning (Flutter)
  - [ ] 3.1 Camera screen, OCR review screen, fallback manual input
    - _Yêu cầu: 11.1, 11.3, 11.5_
  - [ ] 3.2 Auto-classification suggestion UI trong add transaction screen
    - _Yêu cầu: 7.4_
