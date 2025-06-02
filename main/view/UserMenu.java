package main.view;

import main.controller.UnifiedAuthController;
import java.util.Scanner;

public class UserMenu {
    private UnifiedAuthController authController;

    public UserMenu() {
        this.authController = new UnifiedAuthController();
    }

    public void run(Scanner sc) {
        authController.handleAuthMenu(sc);
    }

    public UnifiedAuthController getAuthController() {
        return authController;
    }
} 
