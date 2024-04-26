package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.BuyAndSellStockDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.Stock;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.entity.UserStock;
import com.example.paydaytrade.enums.Exceptions;
import com.example.paydaytrade.exceptions.ApplicationException;
import com.example.paydaytrade.repository.StockRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.repository.UserStockRepository;
import com.example.paydaytrade.service.IStockService;
import com.example.paydaytrade.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class StockService implements IStockService {

    private final StockRepository stockRepository;
    private final UserStockRepository userStockRepository;
    private final ThirdPartyStockServiceClient stockClient;
    private final UserRepository userRepository;
    private final IWalletService walletService;
    private final MailSenderService mailSenderService;

    @Override
    public Stock getStockByName(String name) {
        stockRepository.findStockByName(name).orElseThrow(() -> new ApplicationException(Exceptions.STOCK_NOT_FOUND_EXCEPTION));
        return null;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public GeneralResponseDto buyStock(BuyAndSellStockDto request) {
        Optional<User> user = userRepository.findUserByUsernameOrMail(request.getUsername());
        UserStock userStock = userStockRepository.findUserStockBySymbol(request.getStockId());
        Optional<Stock> stock = stockRepository.findStockBySymbol(request.getStockId());
        String a = request.getUsername();
        boolean checkBalance = walletService.checkBalance(request.getPrice(), user.orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND_EXCEPTION)));
        if (request.getPrice() >= stock.orElseThrow(() -> new ApplicationException(Exceptions.STOCK_NOT_FOUND_EXCEPTION)).getPrice()) {
            if (checkBalance) {
                if (userStock != null) {
                    changeStockOffer(userStock, request.getPrice(), user, true);
                    walletService.decreaseWallet(request.getPrice(), user.orElseThrow());
                } else {
                    saveStock(request.getStockId(), request.getPrice(), user, true);
                    walletService.decreaseWallet(request.getPrice(), user.orElseThrow());
                }
                mailSenderService.sendBuyMessage(user.orElseThrow(), request);
                return new GeneralResponseDto("Purchase is successfull!");
            } else {
                throw new ApplicationException(Exceptions.BALANCE_NOT_ENOUGH_EXCEPTION);
            }
        } else {
            saveStock(request.getStockId(), request.getPrice(), user, false);

            return new GeneralResponseDto("Offer added to the system!");
        }
    }

    @Override
    public GeneralResponseDto sellStock(BuyAndSellStockDto request) {
        Optional<User> user = userRepository.findUserByUsernameOrMail(request.getUsername());
        UserStock userStock = userStockRepository.findUserStockBySymbol(request.getStockId());
        if (userStock != null) {
            if (userStock.isBuyStatus() && !userStock.isSellStatus()) {
                if (request.getPrice() <= userStock.getSellPrice()) {
                    userStock.setSellStatus(true);
                    userStock.setSellRequest(false);
                    userStock.setSellPrice(request.getPrice());
                    userStockRepository.save(userStock);

                    walletService.increaseWallet(request.getPrice(), user.orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND_EXCEPTION)));
                    mailSenderService.sendSellMessage(user.orElseThrow(), request);
                    return new GeneralResponseDto("Sell is succesfully completed!");
                } else {
                    userStock.setSellRequest(true);
                    userStock.setSellPrice(request.getPrice());
                    userStockRepository.save(userStock);
                    return new GeneralResponseDto("Offer added to the system!");
                }
            } else {
                throw new ApplicationException(Exceptions.STOCK_STATUS_IS_FALSE_EXCEPTION);
            }
        } else {
            throw new ApplicationException(Exceptions.STOCK_NOT_FOUND_EXCEPTION);
        }
    }


    @Scheduled(initialDelay = 5 * 60 * 1000, fixedRate = 5 * 60 * 1000)
    public void changeStockDataInDb() {

        List<Stock> stockList = stockRepository.findAll();

        for (Stock stock : stockList) {
            int price = ThreadLocalRandom.current().nextInt(100, 1000);
            stock.setPrice(price);
            stockRepository.save(stock);
        }

    }

    public void insertAllStockToDb() throws IOException {
        List<Stock> stocks = stockClient.getStockData();

        for (Stock stock : stocks) {
            int price = ThreadLocalRandom.current().nextInt(100, 1000);
            stock.setPrice(price);
            stockRepository.save(stock);
        }
    }


    public void changeStockOffer(UserStock userStock, double offerPrice, Optional<User> user, boolean buyStatus) {
        userStock.setBuyStatus(buyStatus);
        userStock.setBuyPrice(offerPrice);
        userStockRepository.save(userStock);

        List<UserStock> userStocks = user.orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND_EXCEPTION)).getUserStock();
        userStocks.add(userStock);
        userRepository.save(user.orElseThrow());
    }

    public void saveStock(String symbol, double offerPrice, Optional<User> user, boolean buyStatus) {
        Optional<Stock> stock = stockRepository.findStockBySymbol(symbol);

        UserStock userStock = userStockRepository.save(
                UserStock.builder()
                        .user(user.orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND_EXCEPTION)))
                        .country(stock.orElseThrow(() -> new ApplicationException(Exceptions.STOCK_NOT_FOUND_EXCEPTION)).getCountry())
                        .name(stock.orElseThrow().getName())
                        .currency(stock.orElseThrow().getCurrency())
                        .exchange(stock.orElseThrow().getExchange())
                        .mic_code(stock.orElseThrow().getMic_code())
                        .buyPrice(offerPrice)
                        .symbol(stock.orElseThrow().getSymbol())
                        .type(stock.orElseThrow().getType())
                        .buyStatus(buyStatus)
                        .build()
        );

        List<UserStock> userStocks = user.orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND_EXCEPTION)).getUserStock();
        userStocks.add(userStock);
        userRepository.save(user.orElseThrow());

    }


}
