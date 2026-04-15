package com.apps.conversionservice.entity;










public class UserEntity {

    
    
    private Long id;

    
    private String email;

    
    private String password;

    
    private String role = "ROLE_USER";

    
    private String authProvider;

    public UserEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
}
