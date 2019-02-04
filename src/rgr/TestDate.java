package rgr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rgr.ent.ParagonListing;

public class TestDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDate td = new TestDate();
		System.out.println(td.makeDate());
		//rgr.ent.ParagonListing pgl = new ParagonListing();
		//java.sql.Date d = pgl.makeDateValue("7/3/2018");
		//System.out.println(d.toString());
	}

	
	private String makeDate() {
	
		Date date = new Date();
		String strDateFormat = "YYYY_MMM_dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		return dateFormat.format(date);
	
	}
}
