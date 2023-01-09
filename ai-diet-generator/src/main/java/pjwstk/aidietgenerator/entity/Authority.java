package pjwstk.aidietgenerator.entity;

public enum Authority {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), DIETITIAN("ROLE_DIETITIAN"), INFLUENCER("ROLE_INFLUENCER");

    public final String role;

    Authority(String role) {
        this.role = role;
    }
}
