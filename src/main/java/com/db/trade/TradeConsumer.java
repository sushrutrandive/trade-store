package com.db.trade;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.db.trade.info.TradeInfo;

public class TradeConsumer implements Consumer<TradeInfo>, Runnable {
	
private BlockingQueue<TradeInfo> tradeQueue;
private TradeStore store;
	
	public TradeConsumer(BlockingQueue<TradeInfo> queue, TradeStore store) {
		this.tradeQueue = queue;
		this.store = store;
	}
	
	
	@Override
	public Optional<TradeInfo> consume() {
		try {
			Thread.currentThread().sleep(400);// Sleep for 0.4 sec to mimic async operation 
			return Optional.of(this.tradeQueue.poll(5, TimeUnit.SECONDS)); // wait for 5 seconds to 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return Optional.empty();
		
	}
	
	@Override
		public void run() {
			
			while(true) {
				Optional<TradeInfo> trade =this.consume();
				if(trade.isPresent()) {
					System.out.println("Consuming : " +  trade.get().getTradeId());
					try {
						this.store.store(trade.get());
					}catch (RuntimeException e) {
						System.out.println(e.getMessage());
					}
					
					
				}else {
					break;
				}
			}
			
		}

}
