package rgr.ent;

//public class PropertyClassQueryFactory {
//}



/**
 * 
 * 
 * 
 * @author john
 * 
 * Note: this is a class which MUST remain the same between the RETS updater and the RGR-login apps.
 * The reference implementation of this class is the RGR-Login app
 * the COPY is the version included in RETS-UPdater
 */


public class PropertyClassQueryFactory {
	
	
	
	private static java.util.HashMap<String,String>  property_class_queries;
	
	public static String getPClassSelect(String label) {
		
		
		java.util.HashMap<String,String>  property_class_queries;
		
		StringBuilder sb = new StringBuilder();
		

		property_class_queries = new java.util.HashMap<String, String>();
		
		// [0] Apartment
		
		property_class_queries.put(
				rgr.ent.Property_Class.apt.toString(),
				" prop_type in ('Residential Attached') and type_of_dwelling in ('Apartment/Condo','Recreational') "
				
				);
		//[1] Single Family House
		sb.append(" (");
		sb.append("(prop_type in ('Residential Detached') ");
		sb.append(" and type_of_dwelling in ('House with Acreage','House/Single Family','Manufactured','Manufactured with Land','Other','Recreational')) ");
		sb.append("OR"); 
		sb.append("( ");
		sb.append("prop_type in ('Residential Attached') and type_of_dwelling in ('1/2 Duplex'))");
		sb.append(") ");
		
		property_class_queries.put(rgr.ent.Property_Class.sfh.toString(),sb.toString());
		sb = new StringBuilder();
		
		//[2] townhouse
		
		sb.append(" (");
		sb.append("(prop_type in ('Residential Detached') and type_of_dwelling in ('Townhouse')) ");
		sb.append(" or ");
		sb.append("(prop_type in ('Residential Attached') and type_of_dwelling in ('1/2 Duplex','Row House (Non-Strata)','Townhouse','Other','Recreational'))");
		sb.append(" ) ");	
		
		
		
		property_class_queries.put(rgr.ent.Property_Class.ths.toString(),sb.toString());				
		sb = new StringBuilder();	
	
		
		// [3] Land
		sb.append(" (");
		sb.append("(prop_type in ('Vacant Land','Land Only')) "); 
		sb.append("or"); 
		sb.append(" (prop_type in ('Residential Detached') and type_of_dwelling in ('House with Acreage','House/Single Family','Manufactured with Land','Recreational')) ");
		sb.append("or");
		sb.append(" (prop_type in ('Commercial') and type_of_dwelling in ('Agri-Business','Land Commercial')) ");
		sb.append(") "); 
		
		property_class_queries.put(rgr.ent.Property_Class.lnd.toString(),sb.toString());
		
		sb = new StringBuilder();
		// [4]residential_rental("Residential Rental"), 

		
		
		//[5]   pre_sale("Pre Sale"),
		
		
		//[6]   single_family_development ("Single Family Development")
		
		sb.append("(");
		sb.append("(prop_type in ('Land Only')) ");
		sb.append("or ");
		sb.append("(prop_type in ('Residential Detached') and type_of_dwelling in ('House with Acreage','House/Single Family','Manufactured with Land','Recreational') ) ");
		sb.append(" ) ");
		property_class_queries.put(rgr.ent.Property_Class.sfd.toString(),sb.toString());
		sb = new StringBuilder();	
		
		
		//[7]  Commercial Retail
		
		property_class_queries.put(rgr.ent.Property_Class.ret.toString(),
				" prop_type in ('Commercial') and type_of_dwelling in ('Retail','Retail Condo','Retail/Suites','Office/Retail','Restaurant','Business with Property')" );
		// [8] office and warehouse
	
		property_class_queries.put(rgr.ent.Property_Class.ofw.toString(),
				"( prop_type in ('Commercial') and type_of_dwelling in ('Office','Office/Warehouse','Office Condo', 'Retail Condo','Industrial')) or  (prop_type in ('Industrial') and type_of_dwelling in ('Warehouse Condo','Warehouse','Office/Warehouse','Multi-Bay Warehouse','Auto Service','Office Condo'))" );
		//bfs("Business for Sale"),  //9

		property_class_queries.put(rgr.ent.Property_Class.bfs.toString(),
				" prop_type in ('Commercial') and type_of_dwelling in ('Business','Business with Property','Restaurant') ");
		
		//cls("Commercial Leasing"),  //10
		property_class_queries.put(rgr.ent.Property_Class.cls.toString(),
				" prop_type in ('Commercial') and type_of_dwelling in ('Agri-Business','Business','Business with Property','Industrial','Land Commercial','Multi-Family Commercial','Retail','Office') ");
		
		/*
		multi_family_development("Multi Family Development"),  //  11
		*/
		sb = new StringBuilder();
		
		sb.append("( prop_type in ('Multi-Family','Multifamily') )"); 
		//and type_of_dwelling in ('Low Rise','Six-Plex','Five-Plex','High Rise','Townhouse') ) ");
		sb.append(" or ( prop_type in ('Commercial') and type_of_dwelling in ('Land Commercial','Retail','Multi-Family Commercial','Industrial','Office','Office/Retail','Office/Warehouse','Retail/Suites','Airspace Sales','Auto Service','Auto Sales','Hotel','Institutional','Shopping Centre','Gas Station','Redevelopment Site','Restaurant','Religious','SRO') )");
		sb.append(" or ( prop_type in ('Industrial') and type_of_dwelling in ('Redevelopment Site','Warehouse','Multi-Bay Warehouse','Office/Warehouse') )");
		sb.append(" or ( prop_type in ('Vacant Land') and type_of_dwelling in ('Redevelopment Site','Warehouse Condo')) ");	
				
		property_class_queries.put(rgr.ent.Property_Class.mfd.toString(),sb.toString());

		
		sb = new StringBuilder();
		sb.append("(");
		sb.append("( ");
		sb.append(" prop_type in ('Commercial') and type_of_dwelling in ");
		sb.append("	('Retail/Suites', 'Hotel', 'Institutional', 'Religious','SRO','Multi-Family Commercial') ");
		sb.append("	) ");
		sb.append("	or ");
		sb.append(" (");
		sb.append("	prop_type in ('Multi-Family', 'Multifamily') and type_of_dwelling in ");
		sb.append("	('Low Rise','Six-Plex', 'Five-Plex','High Rise','Townhouse','Triplex','Fourplex','Duplex','Other','Recreational' ) ");
		sb.append(" ) ");
		sb.append(") ");
		property_class_queries.put(rgr.ent.Property_Class.mfb.toString(),sb.toString());
		
		
		
		
		if(property_class_queries.containsKey(label)) {
			return property_class_queries.get(label);
		}else {
			return "";
		}
		
		
		//return sb.toString();
	}

}


