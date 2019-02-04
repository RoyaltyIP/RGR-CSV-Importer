package rgr;

public class CleanupData {

	public static void main(String[] args) {
		System.out.println("begin execution");
		long l1 = System.nanoTime();

		//rgr.PGConnector.updateSoldTableAfterImport();
		//rgr.PGConnector.updateType_of_Dwelling() ;
		//rgr.PGConnector.NormalizeType_of_dwellingSoldTableAfterImport();
		//rgr.PGConnector.updateType_of_Dwelling();

		doUpdateProp_Classes();
		long l2 = System.nanoTime();
		long elapsed = (l2-l1)/ 1000000000;
		System.out.println("finish execution: "+elapsed+" seconds");
	}
	
	
	 public static void doUpdateProp_Classes() {
		  
		  System.out.println("do update SOLD prop classes");
		  java.util.ArrayList<String> updates = new java.util.ArrayList<String>();
			
		  StringBuilder sb = new StringBuilder();
			
		  sb.append("update para_listing_sold set apt='true' where apt='false' and "); 
		  sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.apt.toString()));
			//System.out.println(sb.toString());
		  updates.add(sb.toString());
			
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set sfh='true' where sfh='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.sfh.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set ths='true' where ths='false' and  ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.ths.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
		
			sb = new StringBuilder();
			sb.append("update para_listing_sold set lnd='true' where lnd='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.lnd.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
			// rrn ( Residential Rental ) is not implemented
			//sb = new StringBuilder();
			//sb.append("update para_listing_sold set rrn='true' where rrn='false' ");
			//sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.rrn.toString()));
			//System.out.println(sb.toString());
			//updates.add(sb.toString());
			
			//psl (Pre-Sale) is not implemented
			//sb = new StringBuilder();
			//sb.append("update para_listing_sold set psl='true' where psl='false' and ");
			//sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.psl.toString()));
			//System.out.println(sb.toString());
			//updates.add(sb.toString());
		
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set sfd='true' where sfd='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.sfd.toString()));
			//System.out.println(sb.toString());
			
			
			updates.add(sb.toString());
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set ret='true' where ret='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.ret.toString()));
			
			//System.out.println(sb.toString());
			
			updates.add(sb.toString());
			
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set ofw='true' where ofw='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.ofw.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set bfs='true' where bfs='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.bfs.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set cls='true' where cls='false' and  ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.cls.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
		
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set mfd='true' where mfd='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.mfd.toString()));
			//System.out.println(sb.toString());
			updates.add(sb.toString());
			
			
			
			sb = new StringBuilder();
			sb.append("update para_listing_sold set mfb='true' where mfb='false' and ");
			sb.append(rgr.ent.PropertyClassQueryFactory.getPClassSelect(rgr.ent.Property_Class.mfb.toString()));
			
			//System.out.println(sb.toString());
			updates.add(sb.toString());

			
			rgr.PGConnector.doRecordInsertsUpdates(updates);
		}
	
	
	

}
