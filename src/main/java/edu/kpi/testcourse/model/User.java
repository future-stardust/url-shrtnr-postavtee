package edu.kpi.testcourse.model;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.Main;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Об'єкт посилання.
 */
public class User {
  private String email;
  private String password;
  private transient String idUser;
  private ArrayList<String> urlList;

  /**
   * Конструктор для юзера.
   *
   * @param email   емейл користувача.
   * @param password   пароль користувача.
   * @param urlList список посилань, створених користувачем.
   */
  public User(String email, String password, ArrayList<String> urlList) {
    this.email = email;
    this.password = password;
    this.urlList = urlList;
  }

  /**
   * Функція, яка повертає емейл користувача.
   *
   * @return email   емейл користувача.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Функція, для встановлення емейла.
   *
   * @param email емейл користувача.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Функція, яка повертає пароль користувача.
   *
   * @return passw   пароль користувача.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Функція, для встановлення пароля.
   *
   * @param password пароль користувача.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Функція, яка повертає ідентифікатор користувача.
   *
   * @return idUser  ідентифікатор користувача.
   */
  public String getIdUser() {
    return idUser;
  }

  /**
   * Функція, для встановлення ідентифікатора.
   *
   * @param idUser ідентифікатор користувача.
   */
  public void setIdUser(String idUser) {
    this.idUser = idUser;
  }

  /**
   * Функція, яка повертає список посилань користувача.
   *
   * @return idUser  ідентифікатор користувача.
   */
  public ArrayList<String> getUrlList() {
    return urlList;
  }

  /**
   * Функція, яка створює порожній список для посилань користувача.
   */
  public void createUrlList() {
    this.urlList = new ArrayList<>();
  }

  /**
   * Функція, для додавання посилання в список.
   */
  public void addInUrlList(String shortUrl) {
    urlList.add(shortUrl);
  }

  /**
   * Функція, для видалення посилання зі списку.
   */
  public void removeFromUrlList(String shortUrl) {
    urlList.remove(shortUrl);
  }

  /**
   * Функція, яка порівнює дані користувача.
   *
   * @return булевий результат, який вказує чи співпадають дані.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(email, user.email) && Objects
      .equals(password, user.password) && Objects.equals(idUser, user.idUser);
  }

  /**
   * Функція, яка хешує дані користувача.
   *
   * @return захешовані дані користувача.
   */
  @Override
  public int hashCode() {
    return Objects.hash(email, password, idUser);
  }

  /**
   * Функція, яка конвертує дані користувача в тип String.
   *
   * @return конвертовані дані користувача в String.
   */
  @Override
  public String toString() {
    return "User{"
      + "email='" + email + '\''
      + ", password='" + password + '\''
      + ", idUser='" + idUser + '\''
      + '}';
  }

  /**
   * Конвертує юзера в JSON.
   *
   * @return об'єкт в форматі JSON.
   */
  public JsonObject toJson() {
    return Main.getGson().getAdapter(User.class).toJsonTree(this).getAsJsonObject();
  }
}
