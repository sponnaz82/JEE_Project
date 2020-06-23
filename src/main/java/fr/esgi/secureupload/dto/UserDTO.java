package fr.esgi.secureupload.dto;

import fr.esgi.secureupload.entities.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class UserDTO {

    @Email
    private String email;

    @Length(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH)
    private String password;
}
