package ru.ellada.ecommerce.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The class describes the "Orders" entity.
 * The @Entity annotation says that objects of this class will be processed by hibernate.
 * The @Table (name = "orders") annotation indicates to the "orders" table in which the objects will be stored.
 * The @Getter and @Setter annotation generates getters and setters for all fields.
 * The @NoArgsConstructor annotation generates no-args constructor.
 * The @EqualsAndHashCode annotation generates implementations for the {@code equals} and {@code hashCode} methods inherited
 * by all objects, based on relevant fields.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see User
 * @see Candle
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "user", "candleList"})
public class Order {
    /**
     * The unique code of the object.
     * The @Id annotation says that the field is the key for the current object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order total price.
     */
    private Double totalPrice;

    /**
     * Date when the order was made.
     */
    private LocalDate date;

    /**
     * The first name of the customer who placed the order.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    private String firstName;

    /**
     * The last name of the customer who placed the order.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    private String lastName;

    /**
     * City of delivery of the order.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    private String city;

    /**
     * Delivery address of the order.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    private String address;

    /**
     * Customer email.
     * The @Email annotation says the string has to be a well-formed email address.
     * The @NotBlank annotation says the field should not be empty.
     */
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    /**
     * Customer phone number.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phoneNumber;

    /**
     * Customer post index.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotNull(message = "Почтовый индекс не может быть пустым")
    @Min(value = 5, message = "Почтовый индекс должен содержать 5 цифр")
    private Integer postIndex;

    /**
     * List of products in the order.
     * Between the {@link Order} and {@link Candle} objects, there is a many-to-many relationship, that is,
     * every record in one table is directly related to every record in another table.
     * Sampling on first access to the current object.
     */
    @OrderColumn
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Candle> candleList;

    /**
     * The customer who made the order.
     * Between the {@link Order} and {@link User} objects, there is a many-to-one relationship, that is,
     * each record in one table is directly related to a single record in another table.
     */
    @ManyToOne
    private User user;

    public Order(User user) {
        this.date = LocalDate.now();
        this.user = user;
        this.candleList = new ArrayList<>();
    }
}
