package core.view;

import org.junit.Assert;
import org.junit.Test;


public class RentViewText {

	@Test
	public final void testMethodIncreaseTenPercentInValue() {
		RentView rentView = new RentView();
		Assert.assertEquals(new Double("110"), rentView.increaseTenPercentInValue(new Double("100")));
	}
}
