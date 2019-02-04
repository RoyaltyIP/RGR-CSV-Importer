package rgr.ent;

/*

create table para_sold_listing (
psl_id serial primary key, 
mls varchar(20) not null default '',
prop_type varchar(256) not null default '',
status varchar(30) not null default '',
address varchar(256) not null default '',
type_of_dwelling varchar(256) not null default '',
sold_price numeric(16,2) not null default '0.0',
list_price numeric(16,2) not null default '0.0',
days_to_sold integer not null default '0',
tot_bedrooms integer not null default '0',
year_built integer not null default '0',
lot_sz_sqft integer not null default '0',
sold_date date default '2008-01-20')

create table para_sold_listing (
psl_id serial primary key, 
mls varchar(20) not null default '',
prop_type varchar(256) not null default '',
status varchar(30) not null default '',
address varchar(256) not null default '',
area varchar(256) not null default '',
sub_area varchar(256) not null default '',
type_of_dwelling varchar(256) not null default '',
sold_price numeric(16,2) not null default '0.0',
list_price numeric(16,2) not null default '0.0',
days_to_sold integer not null default '0',
tot_bedrooms integer not null default '0',
year_built integer not null default '0',
lot_sz_sqft integer not null default '0',
sold_date date default '2008-01-20')

 */

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.TreeSet;

public class ParagonListing {
	
	
	//private java.util.LinkedHashMap<String, KeyValueTriple> data_fields;
	private java.util.TreeMap<String, String> string_fields;
	private java.util.TreeMap<String, Integer> integer_fields;
	private java.util.TreeMap<String, java.sql.Date> date_fields;
	private java.util.TreeMap<String, BigDecimal> numeric_fields;  
	
	private static final boolean DEBUG = true;
	private static boolean has_unknown_fields = false;
	private static final java.util.TreeMap<String, String> status_keys = new java.util.TreeMap<String, String>();
	
	public ParagonListing(){ 
		status_keys.put("S", "Sold");
		status_keys.put("s", "Sold");
		status_keys.put("A", "Active");
		status_keys.put("Active", "Active");
		status_keys.put("a", "Active");
		setFieldTypes(); 
		}
	
	public String getMLS(){
		return string_fields.get("mls");
	}
	public boolean hasUnknownField() {
		return has_unknown_fields;
	}
	
	
	@SuppressWarnings("unused")
	public boolean setFieldValue(String database_field_name, String value){
		
		try {
		has_unknown_fields = true;
		if(string_fields.containsKey(database_field_name)){	

			string_fields.put(database_field_name, value);
			has_unknown_fields = false;
		}else if(integer_fields.containsKey(database_field_name)){
			integer_fields.put(database_field_name, makeInteger(value));
			has_unknown_fields = false;
		}else if(date_fields.containsKey(database_field_name)){
			date_fields.put(database_field_name, makeDateValue(value));
			has_unknown_fields = false;
		}else if(numeric_fields.containsKey(database_field_name)){
			try{
				numeric_fields.put(database_field_name, makeBigDecimal(value));
				has_unknown_fields = false;
			}catch(ParseException e){ System.out.println(" line 95 "+e +"\nfield: ["+database_field_name+"]\nvalue ["+value+"]");}
		}
		if(DEBUG && has_unknown_fields) {
			//System.out.println("Warning: field: "+database_field_name +" !EXIST in data object");
		}
	}catch(Exception e) {
		System.out.println("ParagonListing line 101 "+e);
	}
		
		return !has_unknown_fields;
	}
	
	private String specialStringValues(String database_field_name, String value) {
		
		switch(database_field_name) {
			case "status":
			return  translateStatus(value);
			default:
				return value;
		}
	}
	
	private String translateStatus(String status) {
		if(status_keys.containsKey(status)) {
			return status_keys.get(status);
		}else {
			return status;
		}
	}
	
	private BigDecimal makeBigDecimal(String inputString) throws ParseException{
		
		inputString = inputString.replace("$","");
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		String pattern = "#,###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);

		return (BigDecimal) decimalFormat.parse(inputString);
	}
	
	
	/*
	private Float makeFloat(String inputString){
		inputString = inputString.replace("$","");
		inputString = inputString.replace(",","");
		Float f = null;
		java.util.Scanner scanner = new java.util.Scanner(inputString.trim());
		if(scanner.hasNextFloat()){
			f =  scanner.nextFloat(); //(inputString);
		}
		scanner.close();
		return f;
	}
	*/
	
	public java.sql.Date makeDateValue(String inputString){
		java.util.Date date_obj = null;
		java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
		try {
			date_obj = df.parse(inputString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Date(date_obj.getTime());
	}
	
	private Integer makeInteger(String inputString){
		int i = 0;
		java.util.Scanner scanner = new java.util.Scanner(inputString);
		if (scanner.hasNextInt()) {
		    i = scanner.nextInt();
		}
		scanner.close();
		return new Integer(i);
	}
	
	
	private void setFieldTypes(){
		
		java.sql.Date sql_d = makeDateValue("01/20/1970");
		string_fields = new java.util.TreeMap<String, String>();
		integer_fields = new java.util.TreeMap<String, Integer>();
		date_fields = new java.util.TreeMap<String, java.sql.Date>();
		numeric_fields = new java.util.TreeMap<String,  BigDecimal>();  

		string_fields.put("mls","");
		string_fields.put("prop_type","");
		string_fields.put("status","");
		string_fields.put("address","");
		string_fields.put("sub_area","");
		string_fields.put("type_of_dwelling","");
		string_fields.put("area","");
		string_fields.put("pid", "");
		string_fields.put("zoning", "");
		string_fields.put("postal_code", "");
		 
		string_fields.put("permitted_land_use","");
		string_fields.put("access_to_property","");
		string_fields.put("type","");
		 
		 numeric_fields.put("sp_lp_ratio",new BigDecimal(0.0));
		 numeric_fields.put("sold_price_per_sqft",new BigDecimal(0.0));
		 numeric_fields.put("sold_price",new BigDecimal(0.0));
		 numeric_fields.put("list_price",new BigDecimal(0.0));
		 numeric_fields.put("frontage_feet",new BigDecimal(0.0));
		
		 integer_fields.put("pic_count", 0);
		 integer_fields.put("days_to_sold",0);
		 integer_fields.put("tot_bedrooms",0);
		 integer_fields.put("year_built",0);
		 integer_fields.put("lot_sz_sqft",0);
		 integer_fields.put("tot_baths", 0);
		
		 date_fields.put("sold_date", sql_d);
		 date_fields.put("list_date", sql_d);	
		 
		 //================== new fields 
		 numeric_fields.put("lot_sz_acres",new BigDecimal(0.0));
		 
		 string_fields.put("region","");  // 256
		 integer_fields.put("num_kitch",-99);
		 integer_fields.put("one_bed_units",-99);
		 integer_fields.put("two_bed_units",-99);
		 integer_fields.put("three_bed_units",-99);
		 
		 date_fields.put("adjust_date", sql_d);
		 integer_fields.put("age",-99);
		 integer_fields.put("agt_hit_count",-99);
		 
		 string_fields.put("appointmt_phone_number", "");
		 integer_fields.put("year_renovated",0);
		 
		 string_fields.put("bach_studio_units", "");  
		 date_fields.put("back_on_market_date", sql_d);
		 integer_fields.put("beds_in_bsmt",0);
		 integer_fields.put("beds_not_in_bsmt", 0);
		 string_fields.put("bldg_permit_approved","");
		 string_fields.put("building_plans","");
		 string_fields.put("buyer","");
		 string_fields.put("bylaw_infrac","");
		 string_fields.put("city","");
		 string_fields.put("class_","");
		 
		 date_fields.put("collapse_date", sql_d);
		 date_fields.put("confirm_sold_date", sql_d);
		 integer_fields.put("days_on_mls",0);
		 string_fields.put("depth","");
		 string_fields.put("dev_permit", "");  // yes/no
		 date_fields.put("expiry_date", sql_d);
		 integer_fields.put("fireplaces",0);
		 
		 integer_fields.put("floor_area_grand_tot", 0);
		 integer_fields.put("floor_area_fin_basement",0);
		 string_fields.put("foundation","");
		 numeric_fields.put("gross_taxes",new BigDecimal(0.0));
		 string_fields.put("how_sold","");
		 string_fields.put("income_as_of_date","");
		 string_fields.put("income_per_annum","");
		 string_fields.put("include_in_ddf" ,""); // yes/no
		 string_fields.put("info_package_avail" ,""); 
		 string_fields.put("jusrisdiction","");
		 string_fields.put("land_lease_expiry_year","");
		 date_fields.put("last_trans_date",sql_d);
		 string_fields.put("less_opr_expens","");
		 date_fields.put("list_date",sql_d);
		 integer_fields.put("num_floor_lvl",0);
		 string_fields.put("net_oper_incom","");
		 string_fields.put("other_units","");
		 string_fields.put("natural_gas","");
		 string_fields.put("municipality","");
		 string_fields.put("sanitary_sewer","");
		 string_fields.put("sale_rent","");
		 string_fields.put("realtor_remarks","");
		 string_fields.put("public_remarks","");
		 string_fields.put("prop_in_alr","");
		 date_fields.put("prev_exp_date", sql_d);
		 numeric_fields.put("prev_price",new BigDecimal(0.0));
		 string_fields.put("potential_rezone", "") ; // max 30 chars
		 
		 integer_fields.put("parking_places_tot",0);
		 integer_fields.put("units_in_dev",0);
		 string_fields.put("unit_entitlement","");
		 integer_fields.put("units_in_strata_plan",0);
		 string_fields.put("title_to_land","");
		 numeric_fields.put("strata_main_fee",new BigDecimal(0.0));
		 string_fields.put("storm_sewer","");
		 integer_fields.put("stories_in_building",0);
		 date_fields.put("status_change_date", sql_d);
		 numeric_fields.put("sp_olp_ratio",new BigDecimal(0.0));
		 string_fields.put("worlprop_com","");
		 string_fields.put("water_supply","");
		 string_fields.put("trees_logged","");

	}
	

	public java.util.TreeMap<String,String> getStringFields(){
		return string_fields;
	}
	public java.util.TreeMap<String, Integer> getIntegerFields(){
		return integer_fields;
	}
	public java.util.TreeMap<String, BigDecimal> getBigDecimalFields(){
		return numeric_fields;
	}
	public java.util.TreeMap<String,java.sql.Date> getDateFields(){
		return date_fields;
	}

	public String makeInsert(){
		StringBuffer sb = new StringBuffer();
		sb.append("insert into para_listing_sold (");
		String key_s = null;
		int le_count = 0;
		
		if(string_fields!=null){
			java.util.Set<String> string_field_keys = string_fields.keySet();
			java.util.Iterator<String> k_it = string_field_keys.iterator();
			
			while(k_it.hasNext()){
				if(le_count>0){  sb.append(",");   }
				key_s = k_it.next();
				sb.append(key_s);
				le_count++;
			}
		}
		if(integer_fields!=null){
			java.util.Set<String> integer_field_keys = integer_fields.keySet();
			java.util.Iterator<String> k_it = integer_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){	sb.append(","); }
				sb.append(k_it.next());
				le_count++;
			}
		}
		if(numeric_fields!=null){
			java.util.Set<String> numeric_field_keys =  numeric_fields.keySet();
			java.util.Iterator<String> k_it = numeric_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){sb.append(",");  }
				sb.append(k_it.next());
				le_count++;
			}
		}
		
		
		if(date_fields!=null){
			java.util.Set<String> date_field_keys =  date_fields.keySet();
			java.util.Iterator<String> k_it = date_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){ sb.append(",");  }
				sb.append(k_it.next());
				le_count++;
			}
		}
		
		
		
		//==============	
		sb.append(") values (");
		
		//===============
		
		le_count = 0;
		
		
		
		if(string_fields!=null){	
		
			java.util.Set<String> string_field_keys =  string_fields.keySet();
			java.util.Iterator<String> k_it = string_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){	sb.append(","); }
				k_it.next();
				sb.append("?");
				le_count++;
			}
		}
		
		if(integer_fields!=null){
		
			java.util.Set<String> integer_field_keys = integer_fields.keySet();
			java.util.Iterator<String> k_it = integer_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){	sb.append(","); }
				sb.append("?");
				k_it.next();
				le_count++;
			}
		}
		
		if(numeric_fields!=null){
		
			java.util.Set<String> numeric_field_keys = numeric_fields.keySet();
			java.util.Iterator<String> k_it = numeric_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){	sb.append(","); }
				k_it.next();
				sb.append("?");
				le_count++;
			}
		}
		
		if(date_fields!=null){	
			java.util.Set<String> date_field_keys = date_fields.keySet();
			java.util.Iterator<String> k_it = date_field_keys.iterator();	
			while(k_it.hasNext()){
				if(le_count>0){	sb.append(",");   }
				k_it.next();
				sb.append("?");
				le_count++;
			}
		}
		 
		sb.append(") ");
		return sb.toString();
	}
	public int getIntValue(String field_name ){
		return integer_fields.get(field_name);
	}
	public java.sql.Date getDateValue(String field_name ){
		return date_fields.get(field_name);
	}
	public java.util.Date getUtilDateValue(String field_name){
		return new java.util.Date(date_fields.get(field_name).getTime());
	}
	
	public String getStringValue(String field_name ){
		return string_fields.get(field_name);
	}
	public BigDecimal getNumericValue(String key) {
		return numeric_fields.get(key);
	}
	
	
	
	public String makeUpdate(java.util.TreeSet<String> fields_active) {
		StringBuffer sb = new StringBuffer();
		sb.append("update para_listing_sold set ");//sold_price=?, sold_date=?, days_to_sold=? 
		
		int le_count = 0;
		
		if(string_fields!=null){
			String key_s = "";
			java.util.Set<String> string_field_keys = string_fields.keySet();
			java.util.Iterator<String> k_it = string_field_keys.iterator();
			
			while(k_it.hasNext()){
				
				key_s = k_it.next();
				if(!key_s.equals("mls") && fields_active.contains(key_s)) {
					
					if(le_count>0){  sb.append(",");   }
					sb.append(key_s);
					sb.append("=? ");
					le_count++;
				}
			}
		}
		
		
		
		if(integer_fields!=null){
			String key_i = "";
			java.util.Set<String> integer_field_keys = integer_fields.keySet();
			java.util.Iterator<String> i_it = integer_field_keys.iterator();
			
			while(i_it.hasNext()){
				key_i = i_it.next();
				if(fields_active.contains(key_i)){
					if(le_count>0){  sb.append(",");   }
					sb.append(key_i);
					sb.append("=? ");
					le_count++;
				}
			}
		}
	
		
		if(numeric_fields!=null){
			String key_n = "";
			java.util.Set<String> numeric_field_keys = numeric_fields.keySet();
			java.util.Iterator<String> n_it = numeric_field_keys.iterator();
			
			while(n_it.hasNext()){
				key_n = n_it.next();
				if(fields_active.contains(key_n) ){
					if(le_count>0){  sb.append(",");   }
					sb.append(key_n);
					sb.append("=? ");
					le_count++;
				}
			}
		}
		
		
		if(date_fields!=null){
			String key_d = "";
			java.util.Set<String> date_field_keys = date_fields.keySet();
			java.util.Iterator<String> d_it = date_field_keys.iterator();
			
			while(d_it.hasNext()){
				key_d = d_it.next();
				if(fields_active.contains(key_d)){
					if(le_count>0){  sb.append(",");   }
					sb.append(key_d);
					sb.append("=? ");
					le_count++;
				}
			}
		}
		
	
		sb.append("where mls=?");
		return sb.toString();
	}
	
	

	
	
}
