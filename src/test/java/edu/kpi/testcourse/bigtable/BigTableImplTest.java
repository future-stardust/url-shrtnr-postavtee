package edu.kpi.testcourse.bigtable;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import edu.kpi.testcourse.rest.UsersController;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import java.security.SecureRandom;
import java.util.Random;
import javax.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BigTableImplTest {

  @Test
  void checkValueSaving() {
    BigTableImpl bigTable = new BigTableImpl();

    /* Adding props to user object */
    JsonObject userObject = new JsonObject();
    userObject.addProperty("email", "test@mail.com");
    userObject.addProperty("password", "testPassword");
    userObject.add("userLinks", new JsonArray());

    /* Saving and getting user from db */
    bigTable.saveUserInDb("testKey", userObject);
    JsonObject getUser = bigTable.getUserFromDb("testKey");

    /* Comparing saved user and user that we get from the db */
    assertThat(getUser).isEqualTo(userObject);
  }

  private String genData(@NotNull String dataType) {
    final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    int length = new Random().ints(8, 16).findAny().orElse(0);

    StringBuilder sb = new StringBuilder();

    switch (dataType) {
      case "email":
        int atRange = new Random().ints(2, length - 3).findAny().orElse(0);
        int dotRange = new Random().ints(atRange + 2, length - 1).findAny().orElse(0);

        for (int i = 0; i < length; i++) {
          if (i == atRange) {
            sb.append("@");
          } else if (i == dotRange) {
            sb.append(".");
          } else {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
          }
        }
        break;

      case "password":
      case "key":
        for (int i = 0; i < length; i++) {
          sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        break;

      case "link":
        sb.append("https://");
        for (int i = 0; i < length; i++) {
          sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        break;
    }

    return sb.toString();
  }

  @Test
  void checkCreationOfUser() throws ParseException {
    UsersController u = new UsersController();
    JSONParser parser = new JSONParser();
    String email = genData("email");
    String password = genData("password");

    String request = "{"
      + "\"email\":\"" + email + "\","
      + "\"password\":\"" + password + "\","
      + "}";

    JSONObject json = (JSONObject) parser.parse(request);
    HttpResponse<?> answer = u.signUp(json);
    assertThat(answer.toString()).isEqualTo(HttpResponse.status(HttpStatus.CREATED).toString());
  }

  @Test
  void checkDeletionOfUrl() {
    BigTableImpl bigTable = new BigTableImpl();
    String email = genData("email");
    String password = genData("password");
    String url = genData("link");
    String shorturl = genData("link");

    JsonObject userObject = new JsonObject();
    userObject.addProperty("email", email);
    userObject.addProperty("password", password);
    userObject.add("userLinks", new JsonArray());
    bigTable.saveUserInDb("testKey", userObject);

    bigTable.getUserFromDb("testKey");
    bigTable.saveUrlInDb(url, shorturl);
    bigTable.delUrlFromDb(url);

    assertThat(bigTable.getUrlFromDb(url)).isNull();
  }

  @Test
  void checkUserInDataBase() {
    JsonObject userObject = new JsonObject();
    BigTableImpl bigTable = new BigTableImpl();
    String email = genData("email");
    String password = genData("password");
    String key = genData("key");
    String link = genData("link");

    userObject.addProperty("email", email);
    userObject.addProperty("password", password);
    userObject.add(link, new JsonArray());

    bigTable.saveUserInDb(key, userObject);
    JsonObject getUser = bigTable.getUserFromDb(key);

    assertThat(getUser).isEqualTo(userObject);
  }

  @Test
  void checkUrlInDataBase() {
    BigTableImpl bigTable = new BigTableImpl();
    String key = genData("key");
    String link = genData("link");

    bigTable.saveUrlInDb(key, link);
    String getUrl = bigTable.getUrlFromDb(key);

    assertThat(getUrl).isEqualTo(link);
  }
}

