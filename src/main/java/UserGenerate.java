import com.github.javafaker.Faker;

public class UserGenerate {

    public static UserData getRandomUser() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new UserData(email, password, name);
    }
}
