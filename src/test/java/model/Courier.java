package model;

import com.github.javafaker.Faker;

import java.util.Locale;

public class Courier {
    private final static Faker faker = new Faker(new Locale("ru_RU"));
    private String login;
    private String password;
    private String firstName;

    public static Courier random() {
        Courier courier = new Courier();

        courier.firstName = faker.name().firstName();
        courier.login = faker.internet().emailAddress();
        courier.password = faker.internet().password();

        return courier;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
