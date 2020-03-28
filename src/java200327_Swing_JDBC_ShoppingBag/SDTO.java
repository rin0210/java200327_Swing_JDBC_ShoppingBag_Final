package java200327_Swing_JDBC_ShoppingBag;

public class SDTO {
	private String product;
	private String amount;
	private String unitPrice;
	private String totalPrice;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String[] getArray() {
		String[] returnData = new String[4];
		returnData[0] = this.product;
		returnData[1] = this.amount;
		returnData[2] = this.unitPrice;
		returnData[3] = this.totalPrice;

		return returnData;
	}

}
