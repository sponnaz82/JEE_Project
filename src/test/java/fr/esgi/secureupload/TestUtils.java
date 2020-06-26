package fr.esgi.secureupload;

import fr.esgi.secureupload.common.adapters.helpers.SecureRandomTokenGenerator;
import fr.esgi.secureupload.users.adapters.helpers.UserPasswordEncoderImpl;
import fr.esgi.secureupload.users.adapters.repositories.UserJpaEntity;
import fr.esgi.secureupload.users.entities.User;
import fr.esgi.secureupload.users.ports.RandomTokenGenerator;
import fr.esgi.secureupload.users.ports.UserPasswordEncoder;
import fr.esgi.secureupload.common.utils.URLReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestUtils {

    @Value("${spring.mail.port}")
    private String smtpHost;

    private UserPasswordEncoder encoder;
    private RandomTokenGenerator generator;

    public static final String DEFAULT_PASSWORD = "MyPassword12345";

    public TestUtils(@Autowired PasswordEncoder springEncoderBean){
        this.encoder = new UserPasswordEncoderImpl(springEncoderBean);
        this.generator = new SecureRandomTokenGenerator();
    }

    public JSONObject getSentMail(String to) throws JSONException, IOException {

        // Read sent message for Mailhog API.
        JSONArray messages = new JSONArray(URLReader.readStringFromUrl("http://" + smtpHost + ":8025/api/v1/messages"));

        JSONObject sentMessage = null;

        for (int i = 0; i < messages.length(); i ++){
            if (messages.getJSONObject(i)
                    .getJSONObject("Content")
                    .getJSONObject("Headers")
                    .getJSONArray("To")
                    .getString(0).compareTo(to) == 0){
                sentMessage = messages.getJSONObject(i).getJSONObject("Content");
            }
        }
        return sentMessage;
    }

    public User getRandomUser (boolean admin){
        return User.builder()
                .admin(admin)
                .confirmed(true)
                .password(this.encoder.encode(DEFAULT_PASSWORD))
                .confirmationToken(this.generator.generate(32))
                .email(this.getRandomMail()).build();
    }

    public UserJpaEntity getRandomJpaUser (boolean admin){
        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setEmail(this.getRandomMail());
        userJpaEntity.setPassword(this.encoder.encode(DEFAULT_PASSWORD));
        userJpaEntity.setAdmin(admin);
        userJpaEntity.setConfirmed(true);
        userJpaEntity.setConfirmationToken(this.generator.generate(32));
        return userJpaEntity;
    }

    public String getRandomMail (){
        return String.format("%suser@domain.fr", this.generator.generate(6));
    }
}
