package cn.zyroo.common.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EncryptionProvider encryptionProvider;

    public SecurityService(EncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encryptData(String data) {
        return encryptionProvider.encrypt(data);
    }

    public String decryptData(String encryptedData) {
        return encryptionProvider.decrypt(encryptedData);
    }

    public boolean isAdminRole(String role) {
        return "Admin".equals(role) || "SuperAdmin".equals(role);
    }

    public boolean hasPermission(String userRole, String requiredRole) {
        if (userRole == null || requiredRole == null) {
            return false;
        }

        if ("SuperAdmin".equals(userRole)) {
            return true;
        }

        if ("Admin".equals(userRole) && !"SuperAdmin".equals(requiredRole)) {
            return true;
        }

        return userRole.equals(requiredRole);
    }
}