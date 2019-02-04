package rgr;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;




public class PGConnector {
	
	private static java.sql.Connection connection;
	private static PGConnector pgc = new PGConnector( );
	private PGConnector(){ }
	
	public static PGConnector getInstance( ) {      return pgc;}
	public static java.sql.Connection getConnection(){ 
		try {
			if(connection==null || connection.isClosed()){ connection=init();}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR : unable to connect to database ");
		}	
		return connection;
	}

	

	

	
	
	public static void saveParagonListings(java.util.TreeMap<String, rgr.ent.ParagonListing> paragon_listings, java.util.TreeSet<String> fields_active){
		
		if(!paragon_listings.isEmpty()){
			
			try{	
				connection  = getConnection();
				java.util.TreeMap<String, rgr.ent.ParagonListing> listing_to_update = new java.util.TreeMap<String, rgr.ent.ParagonListing>();
				
				java.util.Set<String> mls_nums = paragon_listings.keySet();
				java.util.Iterator<String> mls_it = mls_nums.iterator();
				
				StringBuffer sb = new StringBuffer();
				
				sb.append("select mls from para_listing_sold where status not in ('Active') and mls in (");
				int l_count =0;
				while(mls_it.hasNext()){
					if(l_count>0){
						sb.append(",");
					}
					sb.append("?");
					mls_it.next();
					l_count++;
				}
				sb.append(")");
				java.sql.PreparedStatement select_stmt = connection.prepareStatement(sb.toString());
				mls_nums=null; mls_it = null; l_count =1;
				mls_nums = paragon_listings.keySet();
				mls_it = mls_nums.iterator();
				while(mls_it.hasNext()){
					 select_stmt.setString(l_count,mls_it.next());
					 l_count++;
				}
				ResultSet rs =  select_stmt.executeQuery();
				String mls_num = "";
				while(rs.next()){
					mls_num = rs.getString("mls");
					if(paragon_listings.containsKey(mls_num)){
						listing_to_update.put(mls_num, paragon_listings.remove(mls_num));
					}
				}
				//System.out.println(" size to update " +listing_to_update.size());
				
				if(listing_to_update!=null && listing_to_update.size()>0){
					
					//System.out.println("try to do update: "+listing_to_update.size());
					StringBuffer sbu = null;
					
					java.util.Set<String> upd_keys = listing_to_update.keySet();
					java.util.Iterator<String> upd_it = upd_keys.iterator();
					rgr.ent.ParagonListing ll_upd = new rgr.ent.ParagonListing();
					connection.setAutoCommit(false);
					
					System.out.println(ll_upd.makeUpdate(fields_active));
					
					java.sql.PreparedStatement upd_stmt = connection.prepareStatement(ll_upd.makeUpdate(fields_active));
					
					while(upd_it.hasNext()){
						ll_upd = listing_to_update.get(upd_it.next());
						int le_count = 1;
						
						java.util.TreeMap<String, String> string_fields = ll_upd.getStringFields();
						
						
						if(string_fields!=null){
							String key_s = "";
							java.util.Set<String> string_field_keys = string_fields.keySet();
							java.util.Iterator<String> k_it = string_field_keys.iterator();
							
							while(k_it.hasNext()){
								key_s = k_it.next();
								if(!key_s.equals("mls") && fields_active.contains(key_s)) {
									upd_stmt.setString(le_count,string_fields.get(key_s));
									le_count++;
								}
							}
						}
						
						
						
						java.util.TreeMap<String, Integer> integer_fields = ll_upd.getIntegerFields();
						if(integer_fields!=null) {
							String key_i = "";
							java.util.Set<String> int_field_keys = integer_fields.keySet();
							java.util.Iterator<String> i_it = int_field_keys.iterator();
							
							while(i_it.hasNext()){
								key_i = i_it.next();
								if(fields_active.contains(key_i)) {
									upd_stmt.setInt(le_count,integer_fields.get(key_i));
									le_count++;
								}
							}
						}
						
						
						java.util.TreeMap<String, java.math.BigDecimal> numeric_fields = ll_upd.getBigDecimalFields();
						if(numeric_fields!=null) {
							String key_n = "";
							java.util.Set<String> numeric_field_keys = numeric_fields.keySet();
							java.util.Iterator<String> n_it = numeric_field_keys.iterator();
							
							while(n_it.hasNext()){
								key_n = n_it.next();
								//System.out.println(key_n);
								if(fields_active.contains(key_n)) {
									upd_stmt.setBigDecimal(le_count,numeric_fields.get(key_n));
									le_count++;
								}
							}
						}
						
						
						java.util.TreeMap<String, java.sql.Date> date_fields = ll_upd.getDateFields();
						if(date_fields!=null) {
							String key_d = "";
							java.util.Set<String> date_field_keys = date_fields.keySet();
							java.util.Iterator<String> d_it = date_field_keys.iterator();
							
							while(d_it.hasNext()){
								key_d = d_it.next();
								if(fields_active.contains(key_d)) {
									//System.out.println("["+key_d+"]["+date_fields.get(key_d)+"]");
									upd_stmt.setDate(le_count,date_fields.get(key_d));
									le_count++;
								}
							}
						}
						
						upd_stmt.setString(le_count,ll_upd.getMLS());
						upd_stmt.addBatch();
					}
					upd_stmt.executeBatch();
					connection.commit();
				}
				//#########################################
				
				

				if(paragon_listings!=null && paragon_listings.size()>0) {
					System.out.println("listings to import "+paragon_listings.size());
					
					connection.setAutoCommit(false);//commit trasaction manually
					rgr.ent.ParagonListing pl = new rgr.ent.ParagonListing();
					java.sql.PreparedStatement insert_stmt = connection.prepareStatement(pl.makeInsert());
						//System.out.println(pl.makeInsert());
						
						mls_nums = paragon_listings.keySet();
						mls_it =mls_nums.iterator();
						
						while(mls_it.hasNext()){
							int field_counter = 1;
							pl = paragon_listings.get(mls_it.next());
							
							java.util.TreeMap<String, String> string_fields = pl.getStringFields();
							java.util.Set<String> string_keys = string_fields.keySet();
							java.util.Iterator<String> string_keys_it = string_keys.iterator();
							String string_value = "";
							while(string_keys_it.hasNext()) {
								string_value = string_fields.get(string_keys_it.next());
								insert_stmt.setString(field_counter, string_value);
								field_counter = field_counter+1;
							}
							
							java.util.TreeMap<String, Integer> integer_fields =pl.getIntegerFields();
							java.util.Set<String> integer_keys = integer_fields.keySet();
							java.util.Iterator<String> integer_keys_it = integer_keys.iterator();
							int integer_value = 0;
							while(integer_keys_it.hasNext()) {
								integer_value = integer_fields.get(integer_keys_it.next());
								insert_stmt.setInt(field_counter, integer_value);
								field_counter = field_counter+1;
							}
							
							
							
							java.util.TreeMap<String, java.math.BigDecimal> numeric_fields =pl.getBigDecimalFields();
							java.util.Set<String> numeric_keys = numeric_fields.keySet();
							java.util.Iterator<String> numeric_keys_it = numeric_keys.iterator();
							java.math.BigDecimal numeric_value = new java.math.BigDecimal(0.0);
							while(numeric_keys_it.hasNext()) {
								numeric_value=numeric_fields.get(numeric_keys_it.next());
								insert_stmt.setBigDecimal(field_counter, numeric_value);
								field_counter = field_counter+1;
							}
							
							
							java.util.TreeMap<String, java.sql.Date> date_fields =pl.getDateFields();
							java.util.Set<String> date_keys =  date_fields.keySet();
							java.util.Iterator<String> date_keys_it = date_keys.iterator();
							java.sql.Date date_value = pl.makeDateValue("01/20/1970");
							while(date_keys_it.hasNext()) {
								date_value = date_fields.get(date_keys_it.next());
								//System.out.println(date_value);
								insert_stmt.setDate(field_counter, date_value);
								field_counter = field_counter+1;
							}
							
							
						
							
							
							
							insert_stmt.getWarnings();
							insert_stmt.addBatch();
						}	
						insert_stmt.executeBatch();
						connection.commit();
					
					
				}else {
					
					System.out.println("there were no inserts on this run");
				}
				
				
				// end of do the updates
				
				if(connection!=null){ connection.close();  }
				connection = null;
	
				}catch(SQLException e){
					System.out.println("PostGresConnector.storeNewContentItem: "+e );
					System.out.println(" "+e.getNextException());
				}
			}// end is-empty
		}
	

	public static void NormalizeType_of_dwellingSoldTableAfterImport() {
		java.util.HashMap<String, String> types_of_dwelling = new java.util.HashMap<String, String>();
		
		
		
		types_of_dwelling.put("Triplex","3PLEX");
		types_of_dwelling.put("Fourplex","4PLEX");
		types_of_dwelling.put("Apartment/Condo","APTU");
		types_of_dwelling.put("Duplex","DUPLX");
		types_of_dwelling.put("1/2 Duplex","DUPXH");
		types_of_dwelling.put("House with Acreage","HACR");
		types_of_dwelling.put("House/Single Family","HOUSE");
		types_of_dwelling.put("Manufactured","MANUF");	
		types_of_dwelling.put("Manufactured with Land","MNFLD");
		types_of_dwelling.put("Other","OTHER");
		types_of_dwelling.put("Recreational","RECRE");	
		types_of_dwelling.put("Row House (Non-Strata)","ROWHS");	
		types_of_dwelling.put("Townhouse","TWNHS");
		
		

		try{	
			connection  = getConnection();
			
			
			connection.setAutoCommit(false);//commit transaction manually

			String update_stmt = "update para_listing_sold set type_of_dwelling=? where type_of_dwelling in (?)";				
			java.sql.PreparedStatement pstmt = connection.prepareStatement(update_stmt);
			java.util.Set<String> string_dwelling_keys = types_of_dwelling.keySet();
			java.util.Iterator<String> s_iterator = string_dwelling_keys.iterator();
			
			while(s_iterator.hasNext()) {
				String key_s = s_iterator.next();
				pstmt.setString(1, key_s);
				pstmt.setString(2, types_of_dwelling.get(key_s));
				pstmt.addBatch();
			}

			pstmt.executeBatch();
			connection.commit();

			
			connection.commit();
		}catch(Exception e) {
			System.out.println("updateSoldTableAfterImport() "+e);
		}	
		
		
		
	}
	

	
	private static java.sql.Connection init(){
		connection = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException "+e+"<br />");
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!<br />");
		}
		try {	
			connection = DriverManager.getConnection(	
		
					
				//"jdbc:postgresql://aa1w2fh7xu35vbl.cngclcqagnfu.us-west-2.rds.amazonaws.com:5432/ebdb?user=rgrsiteuser&password=shadrach99");
			
				//"jdbc:postgresql://127.0.0.1:5432/testDataOne","postgres","Saturn5");
					
				"jdbc:postgresql://104.196.224.179:5432/postgres","rgruser","Shadrach99");
				      

			/*
			DB Instance Identifier VBR_licences vrblicences
			username vrbuser
			password Albrecht99
			database name liquorlicences

		*/
			
			 
			//System.out.println("connection succeeded <br />");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console<br />");

			e.printStackTrace();
		}
		if (connection == null) {
			System.out.println("Failed to make connection!\n");
		}
		return connection;
	}


	public static void updateType_of_Dwelling() {
		
		
		try{
			
			java.util.Set<String> stmts = new java.util.HashSet<String>();
			stmts.add("update para_listing set type_of_dwelling='Fourplex' where type_of_dwelling in ('4PLEX')");
			stmts.add("update para_listing set type_of_dwelling='Apartment/Condo' where type_of_dwelling in ('APTU')");
			stmts.add("update para_listing set type_of_dwelling='Duplex' where type_of_dwelling in ('DUPLX')");
			stmts.add("update para_listing set type_of_dwelling='1/2 Duplex' where type_of_dwelling in ('DUPXH')");
			stmts.add("update para_listing set type_of_dwelling='House with Acreage' where type_of_dwelling in ('HACR')");
			stmts.add("update para_listing set type_of_dwelling='House/Single Family' where type_of_dwelling in ('HOUSE')");
			stmts.add("update para_listing set type_of_dwelling='Manufactured'  where type_of_dwelling in ('MANUF')");	
			stmts.add("update para_listing set type_of_dwelling='Manufactured with Land'  where type_of_dwelling in ('MNFLD')	");
			stmts.add("update para_listing set type_of_dwelling='Other'  where type_of_dwelling in ('OTHER')");	
			stmts.add("update para_listing set type_of_dwelling='Recreational'  where type_of_dwelling in ('RECRE')");	
			stmts.add("update para_listing set type_of_dwelling='Row House (Non-Strata)'  where type_of_dwelling in ('ROWHS')");	
			stmts.add("update para_listing set type_of_dwelling='Townhouse'  where type_of_dwelling in ('TWNHS')");	
		
			connection = getConnection();
			Statement batch_statements = connection.createStatement();
			connection.setAutoCommit(false);
			for (String s : stmts) {
				batch_statements.addBatch(s);
			}
			batch_statements.executeBatch();
			connection.commit();
			connection.close();
			connection = null;
		
		}catch(Exception e) {
			System.out.println("PGConnector line 390 "+e);
		}
	}
	


	public static java.util.HashMap<String,java.util.TreeSet<String>> loadDistinctAreas(){
		java.util.HashSet<String> list_of_areas = new java.util.HashSet<String>();
		java.util.HashMap<String,java.util.TreeSet<String>> areas_to_update = new java.util.HashMap<String,java.util.TreeSet<String>>();
		try{
			connection = getConnection();
				
			//java.sql.PreparedStatement stmt = connection.prepareStatement("select distinct area from para_listing_sold where sub_area not in ('') and area not in ('')");

			//ResultSet rs = stmt.executeQuery();
			//while(rs.next()){
			//	list_of_areas.add(rs.getString("area"));
			//}
			//StringBuffer sb = new StringBuffer();
/*
			"Vancouver East");
			"Tsawwassen");
			//"Williams Lake (Zone 27)"
			"Port Coquitlam");
			"New Westminster");
			"Burnaby South");
			"Surrey");
			//"PG City South (Zone 74)"
			//"Harrison Mills / Mt Woodside"
			"Port Moody");
			"Pitt Meadows");
			//"FVREB Out of Town"
			"North Surrey"
			//"Out of Town"
			"Hope");
			"Maple Ridge");
			//"Terrace (Zone 88)"
			//"Fort St. John (Zone 60)"
			//"BCNREB Out of Area"
			//"PG City Central (Zone 72)"
			//"PG Rural West (Zone 77)"
			//"Yarrow"
			"West Vancouver");
			//"Smithers And Area (Zone 54)"
			"Pemberton");
			"Burnaby North");
			"Rosedale");
			"Vancouver West");
			//"Kitimat (Zone 89)"
			//"Prince Rupert (Zone 52)"
			"Abbotsford");
			//"100 Mile House (Zone 10)"
			//"Bowen Island"
			//"Houston (Zone 53)"
			"Coquitlam");
			//"PG City North (Zone 73)"
			"Richmond"
			"Whistler");
			"Harrison Hot Springs");
		//	"Sunshine Coast"
			"Ladner");
			//"Robson Valley (Zone 81)"
			//"VAN Fake Area"
			//"PG City South East (Zone 75)"
			//"PG Rural North (Zone 76)"
			//"Islands-Van. & Gulf"
			"Chilliwack");
			//"PG Rural South (Zone 78)"
			"South Surrey White Rock");
			//"Mackenzie (Zone 69)"
			//"Vanderhoof And Area (Zone 56)"
			"N. Delta");
			"Squamish");
			"North Vancouver");
			"Cloverdale");
			"Cultus Lake");
			//"Quesnel (Zone 28)"
			"Sardis");
			"Langley");
			"Burnaby East");
			//"PG City West (Zone 71)"
			//"Fort St. James (Zone 57)"
			//"Fort Nelson (Zone 64)"
			"Agassiz");
			//"PG Rural East (Zone 80)"
			//"Burns Lake (Zone 55)"
			"Mission");

			*/
			
			
			if(list_of_areas!=null && list_of_areas.size()>0) {
				
				
				
				java.util.Iterator<String> area_iterator = list_of_areas.iterator();
				String p_h = "";
				java.util.TreeSet<String> sub_area_list = null;
				
				while(area_iterator.hasNext()){
					//sb = new StringBuffer();
					p_h = area_iterator.next();
					java.sql.PreparedStatement stmt_select = connection.prepareStatement("select distinct sub_area as sa from para_listing_sold where area in (?)");
					sub_area_list = new java.util.TreeSet<String>();
					stmt_select.setString(1, p_h);
					ResultSet rs_1 = stmt_select.executeQuery();
					while(rs_1.next()) {
						sub_area_list.add(rs_1.getString("sa"));
					}
					areas_to_update.put(p_h, sub_area_list);
				}
				
				
			
			
			}
			
			
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		return areas_to_update;
	}
	
	 public static void doRecordInsertsUpdates(ArrayList<String> stmts)
	  {
	    try{
	      connection = getConnection();
	      Statement batch_statements = connection.createStatement();
	      connection.setAutoCommit(false);
	      for (int i = 0; i < stmts.size(); i++) {
	        batch_statements.addBatch((String)stmts.get(i));
	      }
	      batch_statements.executeBatch();
	      connection.commit();
	      connection.close();
	      connection = null;
	    }catch (SQLException e){
	      System.out.println("PostgresConnector. doNewRecordInserts");
	      e.printStackTrace();
	      System.out.println(e.getNextException());
	    }
	   
	  }

//	public static int countParagonListings(){
//		int how_many = 0;
//		try{
//			connection = getConnection();
//				
//			java.sql.PreparedStatement stmt = connection.prepareStatement("select count(pid) count_pid from paragon_listing");
//
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				how_many = rs.getInt("count_pid");
//			}
//		} catch (SQLException e) {
//	
//			e.printStackTrace();
//		}
//		return how_many;
//	}
//


	

	
	

}