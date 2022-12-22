package ru.ellada.ecommerce.service.Impl;

import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.repos.CandleRepository;
import ru.ellada.ecommerce.service.CandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * The service layer class implements the accessor methods of {@link Candle} objects
 * in the {@link CandleService} interface database.
 * The class is marked with the @Service annotation - an annotation announcing that this class
 * is a service - a component of the service layer. Service is a subtype of @Component class.
 * Using this annotation will automatically search for service beans.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see Candle
 * @see CandleService
 * @see CandleRepository
 */
@Service
public class CandleServiceImpl implements CandleService {
    /**
     * Implementation of the {@link CandleRepository} interface
     * for working with candles with a database.
     */
    private final CandleRepository candleRepository;

    /**
     * Constructor for initializing the main variables of the order service.
     * The @Autowired annotation will allow Spring to automatically initialize objects.
     *
     * @param candleRepository implementation of the {@link CandleRepository} interface
     *                         for working with candles with a database.
     */
    @Autowired
    public CandleServiceImpl(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }

    /**
     * Return list of all candles.
     *
     * @return list of {@link Candle}.
     */
    @Override
    public List<Candle> findAll() {
        return candleRepository.findAll();
    }

    /**
     * Returns list of candles.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param pageable object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    @Override
    public Page<Candle> findAll(Pageable pageable) {
        return candleRepository.findAll(pageable);
    }

    /**
     * Returns list of candles in which the price is in the range between of starting price and ending price.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param startingPrice The starting price of the product that the user enters.
     * @param endingPrice   The ending price of the product that the user enters.
     * @param pageable      object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    @Override
    public Page<Candle> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable) {
        return candleRepository.findByPriceBetween(startingPrice, endingPrice, pageable);
    }

    /**
     * Returns candle by title
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param candleTitle title to return.
     * @param pageable    object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    @Override
    public Page<Candle> findByCandleTitle(String candleTitle, Pageable pageable) {
        return candleRepository.findByCandleTitle(candleTitle, pageable);
    }

    /**
     * Returns candle by title
     * with the value of the input parameter.
     * A {@link Page} is a sublist of a list of objects.
     *
     * @param candleTitle title or part of title/fragrance to return.
     * @param pageable    object that specifies the information of the requested page.
     * @return list of {@link Candle}.
     */
    @Override
    public Page<Candle> findByCandleTitleLike(String candleTitle, Pageable pageable) {
        return candleRepository.findByTitleLike(candleTitle, pageable);
    }

    /**
     * Returns minimum price of candle.
     *
     * @return minimum price {@link Candle}.
     */
    @Override
    public BigDecimal minCandlePrice() {
        return candleRepository.minCandlePrice();
    }

    /**
     * Returns maximum price of candle from the database.
     *
     * @return maximum price {@link Candle}.
     */
    @Override
    public BigDecimal maxCandlePrice() {
        return candleRepository.maxCandlePrice();
    }

    /**
     * Save updated candle.
     *
     * @param candleTitle    candle title to update.
     * @param fragranceNotes fragrance top notes to update.
     * @param description    candle description to update.
     * @param filename       candle image to update.
     * @param price          candle price to update.
     * @param volume         candle volume to update.
     * @param id             the unique code of the candle to update.
     */
    @Override
    public void saveProductInfoById(String candleTitle, String fragranceNotes,
                                    String description, String filename,
                                    Integer price, Integer volume,
                                    Integer anothePrice, Integer anotherVolume, Long id
    ) {
        candleRepository.saveProductInfoById(candleTitle, fragranceNotes, description, filename, price, volume, anothePrice, anotherVolume, id);
    }

    /**
     * Save candle info.
     *
     * @param candle candle object to return.
     * @return The {@link Candle} class object which will be saved in the database.
     */
    @Override
    public Candle save(Candle candle) {
        return candleRepository.save(candle);
    }
}
