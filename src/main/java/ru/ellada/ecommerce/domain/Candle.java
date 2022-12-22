package ru.ellada.ecommerce.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The class describes the "Candle" entity.
 * The @Entity annotation says that objects of this class will be processed by hibernate.
 * The @Getter and @Setter annotation generates getters and setters for all fields.
 * The @NoArgsConstructor annotation generates no-args constructor.
 * The @AllArgsConstructor annotation generates all args constructor.
 * The @EqualsAndHashCode annotation generates implementations for the {@code equals} and {@code hashCode} methods inherited
 * by all objects, based on relevant fields.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "candleTitle", "candleType", "price", "anotherPrice"})
public class Candle {

    /**
     * The unique code of the object.
     * The @Id annotation says that the field is the key for the current object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Candle title.
     * The @NotBlank annotation says the field should not be empty.
     * Max length of candle title field is 255 characters.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String candleTitle;

    /**
     * Fragrance notes.
     * The @NotBlank annotation says the field should not be empty.
     * Max length of fragranceNotes field is 255 characters.
     */
    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String fragranceNotes;

    /**
     * Candle description.
     */
    private String description;

    /**
     * Candle image.
     */
    private String filename;

    /**
     * Candle price.
     * The @NotBlank annotation says the field should not be empty.
     */
    @NotNull(message = "Пожалуйста заполните поле")
    private Integer price;

    /**
     * Candle volume.
     * The @NotNull annotation says the field should not be empty.
     */
    @NotNull(message = "Пожалуйста заполните поле")
    private Integer volume;

    /**
     * Candle another volume price.
     * The @NotNull annotation says the field should not be empty.
     */
    @NotNull(message = "Пожалуйста заполните поле")
    private Integer anotherPrice;

    /**
     * Candle volume.
     * The @NotNull annotation says the field should not be empty.
     */
    @NotNull(message = "Пожалуйста заполните поле")
    private Integer anotherVolume;

}
