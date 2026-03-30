# Tasks - Báo Cáo, Đồng Bộ và Sao Lưu

- [ ] 1. Implement Reports (Backend)
  - [ ] 1.1 Tạo ReportService và ReportController
    - Generate PDF và Excel, lưu lên S3, auto-delete sau 30 ngày
    - _Yêu cầu: 9.1, 9.2, 9.3, 9.5_
  - [ ]\* 1.2 Viết tests cho report generation
    - _Yêu cầu: 9.3, 9.5_

- [ ] 2. Implement Sync Service (Backend)
  - [ ] 2.1 Tạo SyncService và SyncController
    - Pull/push endpoints, conflict resolution (last-write-wins), batch sync
    - _Yêu cầu: 10.1, 10.2, 10.4, 10.5, 10.6_
  - [ ]\* 2.2 Viết tests cho conflict resolution
    - _Yêu cầu: 10.5_

- [ ] 3. Implement Backup Service (Backend)
  - [ ] 3.1 Tạo BackupService và BackupController
    - Export/encrypt/upload, restore, giữ tối đa 30 backups, scheduled daily backup
    - _Yêu cầu: 18.1, 18.2, 18.3, 18.4, 18.6, 18.7_
  - [ ]\* 3.2 Viết tests cho backup và restore
    - _Yêu cầu: 18.4, 18.6_

- [ ] 4. Implement Reports, Sync, Backup (Flutter)
  - [ ] 4.1 Implement report generation screen
    - Date range selector, generate và download/share PDF/Excel
    - _Yêu cầu: 9.1, 9.4, 9.6_
  - [ ] 4.2 Implement sync service (offline-first)
    - Local SQLite storage, background sync khi có network, sync status indicator
    - _Yêu cầu: 10.2, 10.3, 10.4, 10.5_
  - [ ] 4.3 Implement backup và restore screens
    - Manual backup, restore với confirmation dialog
    - _Yêu cầu: 18.1, 18.4, 18.5_
