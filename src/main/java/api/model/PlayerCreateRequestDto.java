package api.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class PlayerCreateRequestDto {
    private String age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;


    public PlayerCreateRequestDto(String age, String gender, String login, String password, String role, String screenName) {
        this.age = age;
        this.gender = gender;
        this.login = login;
        this.password = password;
        this.role = role;
        this.screenName = screenName;
    }

    public PlayerCreateRequestDto() {
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Map<String, Object> allParamsToMap() {
        ObjectMapper playerCreateParams = new ObjectMapper();
        return playerCreateParams.convertValue(this, Map.class);
    }

}
