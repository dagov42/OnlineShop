package ru.ellada.ecommerce.repos;

import org.springframework.data.repository.query.Param;
import ru.ellada.ecommerce.domain.Candle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * A repository for {@link Candle} objects providing a set of JPA methods for working with the database.
 * Inherits interface {@link JpaRepository}.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see Candle
 * @see JpaRepository
 */
public interface CandleRepository extends JpaRepository<Candle, Long> {
    /**
     * Returns list of candles from the database.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param pageable object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findAll(Pageable pageable);

    /**
     * Returns list of candles from the database in which the price is in the range between of starting price and ending price.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param startingPrice The starting price of the product that the user enters.
     * @param endingPrice   The ending price of the product that the user enters.
     * @param pageable      object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable);

    /**
     * Returns list of candle from the database
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param candleTitle  candle title to return.
     * @param pageable      object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findByCandleTitle(String candleTitle, Pageable pageable);

    /**
     * Returns minimum price of candle from the database.
     * The @Query annotation to declare finder queries directly on repository methods.
     *
     * @return minimum price {@link Candle}.
     */
    @Query(value = "SELECT min(price) FROM Candle")
    BigDecimal minCandlePrice();

    /**
     * Returns maximum price of candle from the database.
     * The @Query annotation to declare finder queries directly on repository methods.
     *
     * @return maximum price {@link Candle}.
     */
    @Query(value = "SELECT max(price) FROM Candle")
    BigDecimal maxCandlePrice();


    /**
     * Returns list of candle from the database
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param title  candle title to return.
     * @param pageable      object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    @Query("SELECT c FROM Candle c WHERE c.fragranceNotes LIKE %:title% OR c.candleTitle LIKE %:title%")
    Page<Candle> findByTitleLike(@Param("title") String title, Pageable pageable);

    /**
     * Save updated candle to the database.
     * The @Modifying annotation declaring manipulating queries.
     * The @Transactional annotation - before the execution of the method marked with this annotation,
     * a transaction starts, after the method is executed, the transaction is committed,
     * and when a RuntimeException is thrown, it is rolled back.
     * The @Query annotation to declare finder queries directly on repository methods.
     *
     * @param candleTitle          candle title to update.
     * @param fragranceNotes       fragrance notes to update.
     * @param description           candle description to update.
     * @param filename              candle image to update.
     * @param price                 candle price to update.
     * @param volume                candle volume to update.
     * @param id                    the unique code of the candle to update.
     */
    @Modifying
    @Transactional
    @Query("update Candle c set c.candleTitle = ?1, c.fragranceNotes = ?2," +
            "c.description = ?3, c.filename = ?4, c.price = ?5, c.volume = ?6, c.anotherPrice = ?7, c.anotherVolume = ?8 where c.id = ?9")
    void saveProductInfoById(String candleTitle, String fragranceNotes,
                             String description, String filename,
                             Integer price, Integer volume, Integer anotherPrice, Integer anotherVolume, Long id);
}