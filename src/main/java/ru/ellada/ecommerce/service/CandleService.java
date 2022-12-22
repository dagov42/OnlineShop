package ru.ellada.ecommerce.service;

import ru.ellada.ecommerce.domain.Candle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.ellada.ecommerce.service.Impl.CandleServiceImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * The service layer interface describes a set of methods for working with objects of the {@link Candle} class.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see Candle
 * @see CandleServiceImpl
 */
public interface CandleService {
    /**
     * Return list of all candles.
     *
     * @return list of {@link Candle}.
     */
    List<Candle> findAll();

    /**
     * Returns list of candles.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param pageable object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findAll(Pageable pageable);

    /**
     * Returns list of candles in which the price is in the range between of starting price and ending price.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param startingPrice The starting price of the product that the user enters.
     * @param endingPrice   The ending price of the product that the user enters.
     * @param pageable      object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable);

    /**
     * Returns list of candle
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param candleTitle candle title to return.
     * @param pageable    object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findByCandleTitle(String candleTitle, Pageable pageable);

    /**
     * Returns list of candle
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param candleTitle candle title or part of title/fragrance to return.
     * @param pageable    object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    Page<Candle> findByCandleTitleLike(String candleTitle, Pageable pageable);

    /**
     * Returns minimum price of candle.
     *
     * @return minimum price {@link Candle}.
     */
    BigDecimal minCandlePrice();

    /**
     * Returns maximum price of candle from the database.
     *
     * @return maximum price {@link Candle}.
     */
    BigDecimal maxCandlePrice();

    /**
     * Save updated candle.
     *
     * @param candleTitle    candle title to update.
     * @param fragranceNotes fragrance notes to update.
     * @param description    candle description to update.
     * @param filename       candle image to update.
     * @param price          candle price to update.
     * @param volume         candle volume to update.
     * @param id             the unique code of the candle to update.
     */
    void saveProductInfoById(String candleTitle, String fragranceNotes, String description,
                             String filename, Integer price, Integer volume, Integer anotherPrice, Integer anotherVolume, Long id);

    /**
     * Save candle info.
     *
     * @param candle object to return.
     * @return The {@link Candle} class object which will be saved in the database.
     */
    Candle save(Candle candle);
}