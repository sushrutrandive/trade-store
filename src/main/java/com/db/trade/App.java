package com.db.trade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import com.db.trade.info.TradeInfo;
import com.sun.source.tree.AssertTree;




public class App 
{
    public static void main( String[] args ) throws Exception
    {
        App app = new App();
        app.start();
    }
    
    public void start() throws InterruptedException {
    	
    	BlockingQueue<TradeInfo> queue =  new ArrayBlockingQueue<TradeInfo>(100);
    	List<TradeInfo> p1TradeList =  p1List();
    	List<TradeInfo> p2TradeList =  p2List();
    	Runnable p1 = new TradeProducer(queue,p1TradeList);
    	Runnable p2 = new TradeProducer(queue,p2TradeList);
    	
    	TradeStore store =  new TradeStore();
    	Runnable c1=  new TradeConsumer(queue,store);
    	
    	ExecutorService service = Executors.newFixedThreadPool(3);
    	service.submit(p1);
    	service.submit(p2);
    	service.submit(c1);
    	
    	
    	service.shutdown();
    	System.out.println("Waiting to complete execution");
    	service.awaitTermination(6, TimeUnit.SECONDS); 
    	System.out.println("execution completed...");
    	List<TradeInfo> list = store.getStoreSnapshot();
    	
    	for(TradeInfo trade : list) {
    		System.out.println(trade);
    	}
    	
    	Assert.assertEquals("Store Size should be 4",4, list.size());
    	Assert.assertTrue("Store should have trade with TradeId ==T1 && Version == 10",
    						list.contains(TradeInfo.builder().withTradeId("T1").withVersion(10).build()));
    	// Calculate future date
    	LocalDate fd = LocalDate.now().plusDays(10);
    	boolean matchFound= list.stream().anyMatch((TradeInfo info)->{
    		 return "T2".equals(info.getTradeId()) && info.getVersion()==1 &&  fd.isEqual(info.getMaturityDate());
    	 });    	
    	Assert.assertTrue("Store should overwritten trade with TradeId ==T2 && Version == 1 && maturity date as 01-Oct-2021",matchFound);
    	
    	matchFound= list.stream().anyMatch((TradeInfo info)->{
   		 return "T5".equals(info.getTradeId()) && info.getVersion()==1 &&  info.isExpired();
   	 });    	
    	Assert.assertTrue("Store should have trade with TradeId ==T5 && Version == 1 && Expired == true",matchFound);
    	
    	
    	
    }
    
    
    
    private List<TradeInfo> p1List(){
    	List<TradeInfo> list = new ArrayList<TradeInfo>();
    	list.add(TradeInfo.builder().withBookId("B1")
    								.withCounterPartyId("C1")
    								.withCreatedDate(LocalDate.of(2021, 1, 1))
    								.withMaturityDate(LocalDate.of(2022, 1, 1))
    								.withTradeId("T1")
    								.withExpired(false)
    								.withVersion(10).build());
    	list.add(TradeInfo.builder().withBookId("B2")
									.withCounterPartyId("C2")
									.withCreatedDate(LocalDate.of(2021, 6, 20))
									.withMaturityDate(LocalDate.of(2022, 1, 1))
									.withTradeId("T2")
									.withExpired(false)
									.withVersion(1).build());
    	// Duplicate Record
    	// Calculate future date
    	LocalDate fd = LocalDate.now().plusDays(10);
    	list.add(TradeInfo.builder().withBookId("B2")
									.withCounterPartyId("C2")
									.withCreatedDate(LocalDate.of(2021, 6, 20))
									.withMaturityDate(fd)
									.withTradeId("T2")
									.withExpired(false)
									.withVersion(1).build());
    	return list;
    }
    
    private List<TradeInfo> p2List(){
    	List<TradeInfo> list = new ArrayList<TradeInfo>();
    	
    	list.add(TradeInfo.builder().withBookId("B1")
    								.withCounterPartyId("C1")
    								.withCreatedDate(LocalDate.of(2021, 1, 1))
    								.withMaturityDate(LocalDate.of(2022, 1, 1))
    								.withTradeId("T3")
    								.withVersion(3).build());
    	// Lower Version
    	list.add(TradeInfo.builder().withBookId("B1")
									.withCounterPartyId("C1")
									.withCreatedDate(LocalDate.of(2021, 1, 1))
									.withMaturityDate(LocalDate.of(2022, 1, 1))
									.withTradeId("T3")
									.withExpired(false)
									.withVersion(1).build());
    	// Maturity Date < Current Date    	
    	list.add(TradeInfo.builder().withBookId("B2")
									.withCounterPartyId("C2")
									.withCreatedDate(LocalDate.of(2015, 6, 20))
									.withMaturityDate(LocalDate.of(2016, 1, 1))
									.withTradeId("T4")
									.withExpired(true)
									.withVersion(1).build());
    	// Maturity Date == Current Date  
    	list.add(TradeInfo.builder().withBookId("B2")
									.withCounterPartyId("C2")
									.withCreatedDate(LocalDate.of(2021, 6, 20))
									.withMaturityDate(LocalDate.now())
									.withTradeId("T5")
									.withExpired(false)
									.withVersion(1).build());
    	// Lower Version from producer 1
    	list.add(TradeInfo.builder().withBookId("B1")
				.withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1))
				.withMaturityDate(LocalDate.of(2022, 1, 1))
				.withTradeId("T1")
				.withVersion(1).build());
    	return list;
    }
    
    
    
    
}
