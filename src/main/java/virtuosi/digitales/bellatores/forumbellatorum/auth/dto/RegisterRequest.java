package virtuosi.digitales.bellatores.forumbellatorum.auth.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Set<Long> categoryIds;
}

