package fr.esgi.secureupload.users.usecases;

import fr.esgi.secureupload.users.exceptions.UserPropertyValidationException;
import fr.esgi.secureupload.users.ports.ConfirmationMailSender;
import fr.esgi.secureupload.users.ports.RandomTokenGenerator;
import fr.esgi.secureupload.users.ports.UserFieldsValidator;
import fr.esgi.secureupload.users.ports.UserPasswordEncoder;
import fr.esgi.secureupload.users.entities.User;
import fr.esgi.secureupload.users.dto.UserDTO;
import fr.esgi.secureupload.users.exceptions.UserMailAlreadyTakenException;
import fr.esgi.secureupload.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CreateUser {

    private final UserRepository repository;
    private final UserPasswordEncoder encoder;
    private final ConfirmationMailSender sender;
    private final RandomTokenGenerator generator;
    private final UserFieldsValidator validator;

    public CreateUser(final UserRepository repository, final UserPasswordEncoder encoder, final ConfirmationMailSender sender, final RandomTokenGenerator generator, final UserFieldsValidator validator){
        this.repository = repository;
        this.encoder = encoder;
        this.sender = sender;
        this.generator = generator;
        this.validator = validator;
    }

    private void sanitizeFields (final UserDTO userDto){
        userDto.setEmail(userDto.getEmail().trim());
    }

    private List<String> checkFields (final UserDTO userDto){

        List<String> errors = new ArrayList<>();

        if (!this.validator.validateMail(userDto.getEmail()))
            errors.add(String.format("%s is not a valid mail address.", userDto.getEmail()));

        if (!this.validator.validatePassword(userDto.getPassword()))
            errors.add("Password does not meet security requirements.");

        return errors;
    }

    public User execute(final UserDTO userDto){

        Objects.requireNonNull(userDto, "User DTO must not be null");
        Objects.requireNonNull(userDto.getEmail(), "Email must not be null");
        Objects.requireNonNull(userDto.getPassword(), "Password must not be null");

        this.sanitizeFields(userDto);

        List<String> errors = this.checkFields(userDto);
        if (errors.size() > 0)
            throw new UserPropertyValidationException(errors);

        if (this.repository.findByEmail(userDto.getEmail()).isPresent())
            throw new UserMailAlreadyTakenException(String.format("Mail %s is already taken.", userDto.getEmail()));

        User user = User.builder()
                .email(userDto.getEmail())
                .password(this.encoder.encode(userDto.getPassword()))
                .admin(false)
                .confirmed(false)
                .confirmationToken(generator.generate(32))
                .recoveryToken(null)
                .build();

        this.sender.sendConfirmationMail(user.getEmail(),

                // (this is a fake front end)
                String.format("http://secureuploadfrontend.com/confirmAccount?id=%s&confirmationToken=%s",
                        user.getEmail(),
                        user.getConfirmationToken()));

        return this.repository.save(user);
    }

}
