package com.foodtrack.web;

import com.foodtrack.entity.Petani;
import com.foodtrack.service.NotificationService;
import com.foodtrack.service.PetaniService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice(basePackages = "com.foodtrack.web")
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    
    private final NotificationService notificationService;
    private final PetaniService petaniService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication auth) {
        Integer targetId = null;
        String targetRole = "ALL";

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String name = auth.getName();
            model.addAttribute("currentUserName", name);
            model.addAttribute("currentUserInitial", name != null && !name.isEmpty() ? name.substring(0, 1).toUpperCase() : "U");
            
            // Check roles
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                targetRole = "ROLE_ADMIN";
            } else {
                targetRole = "ROLE_PETANI";
                Optional<Petani> pOpt = petaniService.findByUsername(name);
                if (pOpt.isPresent()) {
                    targetId = pOpt.get().getIdPetani();
                    model.addAttribute("currentUserPhoto", pOpt.get().getFotoProfile());
                }
            }
        }
        
        model.addAttribute("globalNotifications", notificationService.findRecent(targetId, targetRole));
        model.addAttribute("unreadNotifCount", notificationService.countUnread(targetId, targetRole));
    }
}
