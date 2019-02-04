package rgr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoldListingCleaner {
	
	private StringBuffer the_file;
	
	public static void main(String[] args) {
		
		SoldListingCleaner slc = new SoldListingCleaner();
		slc.doThings();
		
	
		
	}
	
	public void doThings() {
		the_file = new StringBuffer();
		LoadAreas();
		rgr.out.WriterOfFiles.writeAFile(the_file.toString(), "/home/john/git/RGR-CVS-Importer/spreadsheets", "update_statement_"+makeDate()+".txt");
	}
	
	
	private void LoadAreas() {

		java.util.HashMap<String,java.util.TreeSet<String>> areas_to_update = PGConnector.loadDistinctAreas();
		
		java.util.Set<String> area_keys = areas_to_update.keySet();
		java.util.Iterator<String> areas_it = area_keys.iterator();
		String area_key = "";
		while(areas_it.hasNext()) {
			area_key = areas_it.next();
			the_file.append(printSubAreas(area_key, areas_to_update.get(area_key)));
		}
	}
	
	private String makeDate() {
		
		Date date = new Date();
		String strDateFormat = "YYYY_MMM_dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		return dateFormat.format(date);
	
	}
	
	private String printSubAreas(String area_key, java.util.TreeSet<String> sub_areas) {
		StringBuffer sb = new StringBuffer();
		sb.append("update para_listing_sold set area ='");
		sb.append(area_key);
		sb.append("' where sub_area in (");
		int sub_area_counter = 0;
		java.util.Iterator<String> sub_area_it =sub_areas.iterator();
		while(sub_area_it.hasNext()) {
			if(sub_area_counter>0) {
				sb.append(",");
			}
			sb.append("'");
			sb.append(sub_area_it.next());
			sb.append("'");
			sub_area_counter = sub_area_counter+1;
		}
		sb.append(") and area in ('')\n");
		return sb.toString();
		//System.out.println(sb.toString());
	}
}
