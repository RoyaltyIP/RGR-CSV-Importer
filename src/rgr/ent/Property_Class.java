
package rgr.ent;

public enum Property_Class {
	
	apt("Apartment"), //[0]    
	sfh("Single Family House"), // [1]   
	ths("Townhouse"), //[2]  
	lnd("Land"),  // [3]     
	rrn("Residential Rental"),  //[4] 
	psl("Pre Sale"), //[5]
	sfd("Single Family Development"),  //[6]
	ret("Retail"),    //7
	ofw("Office and Warehouse"),  //8
	bfs("Business for Sale"),  //9
	cls("Commercial Leasing"),  //10
	mfd("Multi Family Development"),  //11
	mfb("Multi Family Building"),
	hsc("Hotels and Shopping Center");  //13
	
	//development_application("Development Application"),  // 14
	//rezoning_application("Rezoning Application"),  // 15
	//assignment("Assignment"),   // 16
	//foreclosure("Foreclosure");  // 17

	
	

	
	
	
	/*
	1. Apartment
	2. Single Family House
	3. Retail
	4. Office & Warehouse
	5. Business for sale
	6  Commercial
	7. Residential Rental 
	8. Pre-sale
	9. Multi Family Development
	10. Apartment Building
	11. Land
	12. Foreclosure
	13. Development Application
	14. Rezoning Application
	15. Townhouse & Multi Family
	16. Commercial Leasing
	17. Assignment
	
	*/
	//Single Family Development
	
	
	
	private final String st;
	private Property_Class(final String s){
		st = s;
	}
	
	public String toString() {
        return st;
    }

}

