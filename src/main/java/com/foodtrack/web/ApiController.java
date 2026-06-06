package com.foodtrack.web;

import com.foodtrack.service.NotificationService;
import com.foodtrack.service.PetaniService;
import com.foodtrack.repository.PetaniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor


/**
 * Web Controller untuk ApiController.
 * Menangani request HTTP (GET/POST) dan mengatur respons antarmuka pengguna (View).
 */
public class ApiController {

    private final NotificationService notificationService;
    private final PetaniService petaniService;
    private final PetaniRepository petaniRepository;

    private Map<String, Object> getUserContext(Authentication auth) {
        Integer targetId = null;
        String targetRole = "ALL";
        if (auth != null && auth.isAuthenticated()) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                targetRole = "ROLE_ADMIN";
            } else {
                targetRole = "ROLE_PETANI";
                targetId = petaniService.findByUsername(auth.getName()).map(p -> p.getIdPetani()).orElse(null);
            }
        }
        Map<String, Object> context = new HashMap<>();
        context.put("id", targetId);
        context.put("role", targetRole);
        return context;
    }

    @GetMapping("/unread-count")
    public Map<String, Object> getUnreadCount(Authentication auth) {
        Map<String, Object> ctx = getUserContext(auth);
        Map<String, Object> response = new HashMap<>();
        response.put("count", notificationService.countUnread((Integer)ctx.get("id"), (String)ctx.get("role")));
        return response;
    }

    @PostMapping("/{id}/mark-read")
    public Map<String, Object> markRead(@PathVariable Long id, Authentication auth) {
        notificationService.markAsRead(id);
        Map<String, Object> ctx = getUserContext(auth);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", notificationService.countUnread((Integer)ctx.get("id"), (String)ctx.get("role")));
        return response;
    }

    @PostMapping("/mark-all-read")
    public Map<String, Object> markAllRead(Authentication auth) {
        Map<String, Object> ctx = getUserContext(auth);
        notificationService.markAllAsRead((Integer)ctx.get("id"), (String)ctx.get("role"));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", 0);
        return response;
    }

    @PostMapping("/profile/upload")
    public Map<String, Object> uploadProfile(@RequestBody Map<String, String> payload, 
                                            Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        petaniService.findByUsername(auth.getName()).ifPresent(p -> {
            p.setFotoProfile(payload.get("image"));
            petaniRepository.save(p);
            response.put("success", true);
        });
        return response;
    }
}

