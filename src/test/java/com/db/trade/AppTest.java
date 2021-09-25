package com.db.trade;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.db.trade.info.TradeInfo;

public class AppTest {

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test(expected = RuntimeException.class)
	public void testAddNullValue() {
		TradeStore store = new TradeStore();
		store.store(null);
	}

	@Test
	public void testAddTradeSucessScenario() {

		TradeInfo t1 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1)).withMaturityDate(LocalDate.of(2022, 1, 1)).withTradeId("T1")
				.withExpired(false).withVersion(10).build();

		TradeStore store = new TradeStore();
		store.store(t1);
		Assert.assertEquals("Store Size should be 1", 1, store.getStoreSnapshot().size());
		Assert.assertEquals("Content should match", t1.toString(), store.getStoreSnapshot().get(0).toString());

	}

	@Test
	public void testAddTradeDuplicateScenario() {

		TradeInfo t1 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1)).withMaturityDate(LocalDate.of(2022, 1, 1)).withTradeId("T1")
				.withExpired(false).withVersion(1).build();

		TradeInfo t2 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1)).withMaturityDate(LocalDate.of(2021, 12, 1)).withTradeId("T1")
				.withExpired(false).withVersion(1).build();

		TradeStore store = new TradeStore();
		store.store(t1);
		store.store(t2);
		Assert.assertEquals("Store Size should be 1", 1, store.getStoreSnapshot().size());
		Assert.assertEquals("Content should match with overwritten object", t2.toString(),
				store.getStoreSnapshot().get(0).toString());

	}

	@Test
	public void testAddTradeLowerVersionScenario() {

		TradeInfo t1 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1)).withMaturityDate(LocalDate.of(2022, 1, 1)).withTradeId("T1")
				.withExpired(false).withVersion(10).build();

		TradeInfo t2 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2021, 1, 1)).withMaturityDate(LocalDate.of(2021, 12, 1)).withTradeId("T1")
				.withExpired(false).withVersion(1).build();
		TradeStore store = new TradeStore();
		store.store(t1);
		exceptionRule.expect(RuntimeException.class);
		exceptionRule.expectMessage("LOWER_VERSION_TRADE");
		store.store(t2);
	}

	@Test
	public void testAddTradeWithPastMaturityDateScenario() {

		TradeInfo t1 = TradeInfo.builder().withBookId("B1").withCounterPartyId("C1")
				.withCreatedDate(LocalDate.of(2015, 1, 1)).withMaturityDate(LocalDate.of(2016, 1, 1)).withTradeId("T1")
				.withExpired(false).withVersion(10).build();

		TradeStore store = new TradeStore();
		store.store(t1);

		Assert.assertEquals("Store should not contain trade with previous maturity date trade", 0,
				store.getStoreSnapshot().size());

	}
}
