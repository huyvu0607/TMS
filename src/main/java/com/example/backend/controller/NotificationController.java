package com.example.backend.controller;

import com.example.backend.dto.notification.response.NotificationResponse;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.security.UserDetailsImpl;
import com.example.backend.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho Notification Management
 *
 * Endpoints:
 * - GET    /api/notifications                - Lấy notifications (paginated)
 * - GET    /api/notifications/unread         - Lấy notifications chưa đọc
 * - GET    /api/notifications/unread/count   - Đếm notifications chưa đọc
 * - GET    /api/notifications/{id}           - Lấy notification detail
 * - PUT    /api/notifications/{id}/read      - Đánh dấu đã đọc
 * - PUT    /api/notifications/read-all       - Đánh dấu tất cả đã đọc
 * - DELETE /api/notifications/{id}           - Xóa notification
 * - DELETE /api/notifications/read           - Xóa tất cả đã đọc
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final INotificationService INotificationService;

    /**
     * Lấy danh sách notifications (có phân trang)
     *
     * GET /api/notifications?page=0&size=20
     *
     * Query Params:
     * - page: số trang (default: 0)
     * - size: số items per page (default: 20)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("📋 [GET /api/notifications] User {} xem notifications, page={}, size={}",
                userDetails.getUsername(), page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        PageResponse<NotificationResponse> notifications =
                INotificationService.getUserNotifications(userDetails.getId(), pageable);

        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách notifications thành công",
                notifications
        ));
    }

    /**
     * Lấy notifications chưa đọc
     *
     * GET /api/notifications/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("📋 [GET /api/notifications/unread] User {}", userDetails.getUsername());

        List<NotificationResponse> notifications =
                INotificationService.getUnreadNotifications(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(
                "Lấy notifications chưa đọc thành công",
                notifications
        ));
    }

    /**
     * Đếm số notifications chưa đọc
     * Use case: Badge count (số đỏ trên icon notification)
     *
     * GET /api/notifications/unread/count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> countUnreadNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("📊 [GET /api/notifications/unread/count] User {}", userDetails.getUsername());

        Long count = INotificationService.countUnreadNotifications(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * Lấy notification detail
     *
     * GET /api/notifications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("📋 [GET /api/notifications/{}] User {}", id, userDetails.getUsername());

        NotificationResponse notification =
                INotificationService.getNotificationById(id, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(notification));
    }

    /**
     * Đánh dấu notification đã đọc
     *
     * PUT /api/notifications/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("✅ [PUT /api/notifications/{}/read] User {}", id, userDetails.getUsername());

        INotificationService.markAsRead(id, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("Đã đánh dấu notification đã đọc", null));
    }

    /**
     * Đánh dấu tất cả notifications đã đọc
     *
     * PUT /api/notifications/read-all
     */
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("✅ [PUT /api/notifications/read-all] User {}", userDetails.getUsername());

        INotificationService.markAllAsRead(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("Đã đánh dấu tất cả notifications đã đọc", null));
    }

    /**
     * Xóa notification
     *
     * DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("🗑️ [DELETE /api/notifications/{}] User {}", id, userDetails.getUsername());

        INotificationService.deleteNotification(id, userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("Đã xóa notification thành công", null));
    }

    /**
     * Xóa tất cả notifications đã đọc
     *
     * DELETE /api/notifications/read
     */
    @DeleteMapping("/read")
    public ResponseEntity<ApiResponse<Void>> deleteAllReadNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("🗑️ [DELETE /api/notifications/read] User {}", userDetails.getUsername());

        INotificationService.deleteAllReadNotifications(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("Đã xóa tất cả notifications đã đọc", null));
    }
}