package com.inlog.ecommerce.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class categoryProduct implements Serializable {
	private int productid;
	private String name;
	private String desc;
	private int base_price;
	private int sale_price;
	private String imageurl;
	private String shopName,shop_id;
	private String qty = "0";
	private Boolean isWishListAdded = false;
	private int product_variant_id;
	private int selectedproductVrientPostion = -1;
	private  boolean is_combo;
	private int discount;
	private List<ProdctVariants> productvarient = new ArrayList<>();
	static ArrayList<categoryProduct> wishlist = new ArrayList<>();
	static ArrayList<categoryProduct> MyCard = new ArrayList<>();
	public categoryProduct(){

	}

	public categoryProduct(int productid, String name, String desc, int base_price, int sale_price, String imageurl, int product_variant_id, List<ProdctVariants> productvarient, String shopName, String shop_id, int discount,boolean is_combo)
	{
		this.productid = productid;
		this.name = name;
		this.desc = desc;
		this.base_price = base_price;
		this.sale_price = sale_price;
		this.imageurl = imageurl;
		this.product_variant_id = product_variant_id;
		this.productvarient = productvarient;
		this.shopName = shopName;
		this.shop_id = shop_id;
		this.discount = discount;
		this.is_combo = is_combo;
	}


	public void SetWishList(com.inlog.ecommerce.model.categoryProduct word)
	{
		this.wishlist.add(0, word);

	}
	public ArrayList<categoryProduct> getWishlist(){return wishlist;}
	public void removeWishList(int position)
	{
		wishlist.remove(position);
	}


	public ArrayList<categoryProduct> getMyCard(){return MyCard;}
	public void removeMyCard(int position)
	{
		MyCard.remove(position);
	}
	public void SetMyCard(com.inlog.ecommerce.model.categoryProduct word) {
		MyCard.add(0, word);

	}
	public int getProductid() {
		return productid;
	}
	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public int getBase_price() {
		return base_price;
	}
	public int getSale_price() {
		return sale_price;
	}
	public String getImageurl() {
		return imageurl;
	}
	public int getProduct_variant_id() {
		if(productvarient!=null && productvarient.size()>0 && productvarient.size()>getSelectedproductVrientPostion())
			return productvarient.get(getSelectedproductVrientPostion()).productVariantId;
		else
		return product_variant_id;
	}
    public JSONArray getProductAttributeIds() {
		JSONArray jsoArray = new JSONArray();

		if(productvarient!=null && productvarient.size()>0 && productvarient.size()>getSelectedproductVrientPostion()) {
			ArrayList<ProductAttribute> attrList = productvarient.get(getSelectedproductVrientPostion()).getAttrValueList();
			for (ProductAttribute prdAttribute : attrList) {
				jsoArray.put(prdAttribute.getId());

			}
		}
		return jsoArray;
	}
	public ArrayList<ProductAttribute> getProductAttributeList() {
		if(productvarient!=null && productvarient.size()>0 && productvarient.size()>getSelectedproductVrientPostion()) {
			return productvarient.get(getSelectedproductVrientPostion()).getAttrValueList();
		}
		return new ArrayList<>();
	}
	public void setProductAttributeList(ArrayList<ProductAttribute> attrList) {
		if(productvarient!=null && productvarient.size()>0 && productvarient.size()>getSelectedproductVrientPostion()) {
			 productvarient.get(getSelectedproductVrientPostion()).setAttrValueList(attrList);
		}
	}

	public List<ProdctVariants> getProductvarient() {
		return productvarient;
	}

	public void setProductvarient(List<ProdctVariants> productvarient) {
		this.productvarient = productvarient;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public int getQty() {
		try {
			return Integer.parseInt(qty);
		}catch (Exception ex){
			return 0;
		}
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public Boolean getWishListAdded() {
		return isWishListAdded;
	}

	public void setWishListAdded(Boolean wishListAdded) {
		isWishListAdded = wishListAdded;
	}

	public int getSelectedproductVrientPostion() {
		if(selectedproductVrientPostion == -1) {
			for (int i = 0; i < this.productvarient.size(); i++) {
				ProdctVariants variantObj = productvarient.get(i);
				if (variantObj.getCart_count()>0)
					return i;
			}
			return 0;
		}
		return selectedproductVrientPostion;
	}

	public int getSelectedProductVariantQTY() {
			return this.getProductvarient().get(getSelectedproductVrientPostion()).getCart_count();
	}
   public void setSelectedProductVariantQTY(int cartCount) {
	   this.getProductvarient().get(getSelectedproductVrientPostion()).setCart_count(cartCount);
   }
   public ProdctVariants getSelectedProductVariant() {
	   return this.getProductvarient().get(getSelectedproductVrientPostion());
   }

	public void setSelectedproductVrientPostion(int selectedproductVrientPostion) {
		this.selectedproductVrientPostion = selectedproductVrientPostion;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSale_price(int sale_price) {
		this.sale_price = sale_price;
	}

	public void setProduct_variant_id(int product_variant_id) {
		this.product_variant_id = product_variant_id;
	}
	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public static void setWishlist(ArrayList<categoryProduct> wishlist) {
		categoryProduct.wishlist = wishlist;
	}

	public static void setMyCard(ArrayList<categoryProduct> myCard) {
		MyCard = myCard;
	}
	public boolean isIs_combo() {
		return is_combo;
	}

	public void setIs_combo(boolean is_combo) {
		this.is_combo = is_combo;
	}


}
