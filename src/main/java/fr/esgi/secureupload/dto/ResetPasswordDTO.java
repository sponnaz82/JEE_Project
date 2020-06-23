package fr.esgi.secureupload.dto;

import fr.esgi.secureupload.entities.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordDTO {

    @Length(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH)
    private String currentPassword;

    @Length(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH)
    private String newPassword;
}