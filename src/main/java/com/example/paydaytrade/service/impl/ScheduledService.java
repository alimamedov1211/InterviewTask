package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.BuyAndSellStockDto;
import com.example.paydaytrade.entity.Stock;
import com.example.paydaytrade.entity.UserStock;
import com.example.paydaytrade.repository.UserStockRepository;
import com.example.paydaytrade.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final UserStockRepository userStockRepository;
    private final StockService stockService;
    private final IWalletService walletService;
    private final MailSenderService mailSenderService;


    @Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
    public void buyingProcess() {
        List<UserStock> stockList = userStockRepository.findByBuyStatusFalse();

        if (!stockList.isEmpty()) {
            for (UserStock stock : stockList) {
                Stock stockBySymbol = stockService.getStockByName(stock.getName());

                if (stock.getBuyPrice() >= stockBySymbol.getPrice()) {
                    stock.setBuyStatus(true);
                    stock.setBuyPrice(stockBySymbol.getPrice());
                    userStockRepository.save(stock);

                    BuyAndSellStockDto buySellRequestDto = BuyAndSellStockDto.builder()
                            .username(stock.getUser().getUsername())
                            .stockId(stock.getSymbol())
                            .price(stock.getBuyPrice())
                            .build();

                    walletService.decreaseWallet(stockBySymbol.getPrice(), stock.getUser());
                    mailSenderService.sendBuyMessage(stock.getUser(), buySellRequestDto);

                }
            }
        }
    }

    @Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
    public void checkSellingProcess() {
        List<UserStock> stocks = userStockRepository.findBySellRequestTrue();
        if (!stocks.isEmpty()) {
            for (UserStock stock : stocks) {
                Stock stockBySymbol = stockService.getStockByName(stock.getName());

                if (stock.getSellPrice() <= stockBySymbol.getPrice()) {
                    stock.setSellStatus(true);
                    stock.setSellRequest(false);
                    stock.setSellPrice(stockBySymbol.getPrice());
                    userStockRepository.save(stock);

                    BuyAndSellStockDto buySellRequestDto = BuyAndSellStockDto.builder()
                            .username(stock.getUser().getUsername())
                            .stockId(stock.getSymbol())
                            .price(stock.getBuyPrice())
                            .build();

                    walletService.decreaseWallet(stockBySymbol.getPrice(), stock.getUser());
                    mailSenderService.sendSellMessage(stock.getUser(), buySellRequestDto);

                }
            }
        }
    }


}
