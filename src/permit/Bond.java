package permit;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.text.*;
import java.util.List;
import org.apache.log4j.Logger;


public class Bond implements java.io.Serializable{

    String id="", bond_num="", expire_date="", amount="",company_contact_id="", notes="", bond_company_id="", description="";
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		static final long serialVersionUID = 222L;	
		static Logger logger = Logger.getLogger(Bond.class);
		String permit_id="", user_id="";
    String errors = "";
		CompanyContact companyContact = null;
		Company company = null;
		Contact contact = null;
		Permit permit = null;
		List<Permit> permits = null;
		Type bond_company = null;
		String type = "Excavation"; // Excavation, Grading, Development
	
    public Bond(){

    }
    public Bond(String val){
				setId(val);
    }
    public Bond(String val,
				
								String val2,
								String val3,
								String val4,
								String val5,
								String val6,
								String val7,
								String val8,
								String val9,
								String val10
								){
				setValues(val, val2, val3, val4, val5, val6, val7, val8, val9, val10);
    }
		void setValues(
									 String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8,
									 String val9,
									 String val10
									 ){
				setId(val);
				setBond_company_id(val2);
				setBond_num(val3);
				setExpire_date(val4);
				setAmount(val5);
				setCompany_contact_id(val6);
				setNotes(val7);
				if(val8 != null){
						bond_company = new Type(bond_company_id, val8);
				}
				setDescription(val9);
				setType(val10);
		
		}
		//
		// setters
		//
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setBond_company_id(String val){
				if(val != null)
						bond_company_id = val;
    }	
		public void setBond_num(String val){
				if(val != null)
						bond_num = val;
    }
		public void setAmount(String val){
				if(val != null)
						amount = val;
    }
		public void setType(String val){
				if(val != null)
						type = val;
    }
		public void setCompany_contact_id(String val){
				if(val != null)
						company_contact_id = val;
    }
		public void setNotes(String val){
				if(val != null)
						notes = val;
    }
		public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }
		public void setUser_id(String val){
				if(val != null)
						user_id = val;
    }
		public void setPermit_id(String val){
				if(val != null)
						permit_id = val;
    }
		public void setDescription(String val){
				if(val != null)
						description = val;
    }	
    //
    // getters
    //
		public String getId(){
				return id;
    }
		public String getBond_company_id(){
				return bond_company_id;
    }	
    public String getBond_num(){
				return bond_num;
    }
		public String getExpire_date(){
				return expire_date;
    }
		public String getAmount(){
				return amount;
    }	
		public String getType(){
				return type;
    }
		public String getCompany_contact_id(){
				return company_contact_id;
    }
		public String getNotes(){
				return notes;
    }
		public String getDescription(){
				return description;
    }
		public boolean equals(Object gg){
				boolean match = false;
				if (gg != null && gg instanceof Bond){
						match = id.equals(((Bond)gg).id);
				}
				return match;
		}
		public int hashCode(){
				int code = 0;
				try{
						code = Integer.parseInt(id);
				}catch(Exception ex){};
				return code;
		}	
		public CompanyContact getCompanyContact(){
				if(companyContact == null){
						if(company_contact_id.equals("") && !permit_id.equals("")){
								getPermit();
								findCompanyContactFromPermit();
						}
						if(!company_contact_id.equals("")){
								CompanyContact one = new CompanyContact(company_contact_id);
								String back = one.doSelect();
								if(back.equals("")){
										companyContact = one;
								}
						}
				}
				return companyContact;
		}	
		public Contact getContact(){
				getCompanyContact();
				if(companyContact != null){
						contact = companyContact.getContact();
				}
				return contact;
		}
		public Company getCompany(){
				getCompanyContact();
				if(companyContact != null){
						company =  companyContact.getCompany();
				}
				return company;
		}
		public Permit getPermit(){
				if(permit == null && !permit_id.equals("")){
						Permit one = new Permit(permit_id);
						String back = one.doSelect();
						if(back.equals("")){
								permit = one;
						}
				}
				else if(!company_contact_id.equals("")){
						PermitList  pl = new PermitList();
						pl.setCompany_contact_id(company_contact_id);
						String back = pl.find();
						if(back.equals("")){
								List<Permit> list = pl.getPermits();
								if(list != null && list.size() > 0){
										permit = list.get(0);
										permit_id = permit.getId();
								}
						}
				}
				return permit;
		}
		public List<Permit> getPermits(){
				if(permits == null && !id.equals("")){
						PermitList  pl = new PermitList();
						pl.setBond_id(id);
						pl.setNoLimit();
						String back = pl.find();
						if(back.equals("")){
								List<Permit> list = pl.getPermits();
								if(list != null && list.size() > 0){
										permits = list;
										if(list.size() == 1){
												permit = list.get(0);
										}
								}
						}
				}
				return permits;
		}
		public boolean hasPermits(){
				return getPermits() != null;
		}
		public Type getBond_company(){
				if(bond_company == null && !bond_company_id.equals("")){
						Type one = new Type(bond_company_id, null, "bond_companies");
						String back = one.doSelect();
						if(back.equals("")){
								bond_company = one;
						}
				}
				return bond_company;
		}
		public boolean hasCompany(){
				getCompany();
				return company != null;
		}
		public boolean hasContact(){
				getContact();
				return contact != null;
		}
		public String getPermit_id(){
				return permit_id;
    }
		public boolean hasPermit(){
				getPermit();
				return !permit_id.equals("");
		}	
		private void findCompanyContactFromPermit(){
				if(permit == null){
						getPermit();
				}
				if(permit != null){
						if(permit.hasCompanyContact()){
								companyContact = permit.getCompanyContact();
								company_contact_id = companyContact.getId();
						}
				}
		}	
		public String toString(){
				return bond_num;
		}
		public String getInfo(){
				String ret = "";
				getBond_company();
				if(bond_company != null){
						ret = bond_company.getName();
				}
				if(!bond_num.equals("")){
						ret += " #:"+bond_num;
				}
				if(!expire_date.equals("")){
						ret += " Exp:"+expire_date;
				}
				return ret;
		}
		private String addBondToPermit(){
				String msg = "";
				getPermit();
				if(permit != null){
						permit.setBond_id(id);
						msg = permit.doUpdate();
				}
				return msg;
		}
		public String doSelect(){
		
				String msg="";
		
				String qq = "select b.id,b.bond_company_id,b.bond_num,"+
						" date_format(b.expire_date,'%m/%d/%Y'),b.amount,"+
						" b.company_contact_id,b.notes,c.name, b.description,b.type "+
						" from bonds b "+
						" left join bond_companies c on c.id=b.bond_company_id "+
						" where b.id=?";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect ";
						}
						else{
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1,id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setValues(rs.getString(1),
															rs.getString(2),
															rs.getString(3),
															rs.getString(4),
															rs.getString(5),
															rs.getString(6),
															rs.getString(7),
															rs.getString(8),
															rs.getString(9),
															rs.getString(10)
															);
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(ex+" : "+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doSave(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(hasPermit() && company_contact_id.equals("")){
						findCompanyContactFromPermit();
				}		
				String qq = "insert into bonds values (0,"+
						"?,?,?,?,?,?,?,?)";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);			
						pstmt = con.prepareStatement(qq);
						msg += setFields(pstmt);
						pstmt.executeUpdate();
						qq = "select LAST_INSERT_ID() ";

						logger.debug(qq);

						pstmt = con.prepareStatement(qq);			
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}			
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
    }	
		public String setFields(PreparedStatement pstmt){

				String msg = "";
				String today = Helper.getToday();
				int jj = 1;
				try{
						if(bond_company_id.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, bond_company_id);			
						if(bond_num.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, bond_num);
						if(expire_date.equals(""))
								pstmt.setNull(jj++, Types.DATE);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(expire_date).getTime()));				
						if(amount.equals(""))
								pstmt.setNull(jj++, Types.DOUBLE);
						else						
								pstmt.setString(jj++, amount);
						if(company_contact_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, company_contact_id);
						if(notes.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, notes);
						if(description.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, description);
						if(type.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, type);
			
				}
				catch(Exception ex){
						msg += ex;
						logger.error(msg);
				}
				return msg;
		}
		public String doUpdate(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//
		
				String qq = "";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}		
				try {
						//
						qq = "update bonds set ";
						qq += "bond_company_id =?, ";
						qq += "bond_num =?,"; 
						qq += "expire_date =?, ";
						qq += "amount = ?,";
						qq += "company_contact_id = ?,";
						qq += "notes = ?, ";
						qq += "description = ?, ";
						qq += "type = ? ";
						qq += "where id=? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						setFields(pstmt);
						pstmt.setString(9, id);
						pstmt.executeUpdate();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doDelete(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				if(id.equals("")){
						msg = "company id not set";
						return msg;
				}
				//
				String qq = "delete from bonds where id=?";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(msg);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}			
	
}
