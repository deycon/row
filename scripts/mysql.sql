;;/**
;; * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
;; * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
;; * @author W. Sibo <sibow@bloomington.in.gov>
;; *
;; * Note: this application was built based of an old designed app that need
;; * to be migrated and updated. If you decide to add an attribute to any table
;; * make sure to add similar attribute to the class corresponding to it most
;; * likely will carry similar name or its derivative.
;; */

	 create table users(id int not null auto_increment, empid varchar(8) not null unique, fullname varchar(70), role enum('Edit','Edit:Delete','Edit:Delete:Admin'), active char(1), primary key(id))engine=InnoDB;
;;	 //
;;	 // inspectors and reviewers
	 create table inspectors(user_id int not null unique, office_phone varchar(20), desk_phone varchar(20), cell_phone varchar(12), fax_num varchar(12), primary key(user_id), foreign key(user_id) references users(id))engine=InnoDB;
;;	 
;;
	 create table companies(id int not null auto_increment, name varchar(70), address varchar(70), city varchar(70), state varchar(2), zip varchar(10), phone varchar(30), website varchar(70), notes varchar(500), primary key(id))engine=InnoDB;
	 create table contact_types(id int not null auto_increment, name varchar(30) not null unique, primary key(id));
	 
	 insert into contact_types values(1,'Contractor'),(2,'A&E'),(3,'Property Owner'),(4,'Developer'),(5,"Utility'),(6,"Internal");
;;
	 create table contacts (id int not null auto_increment, fname varchar(70),lname varchar(70), title varchar(70), type_id int, address varchar(70), city varchar(70), state varchar(2), zip varchar(10), work_phone varchar(20), cell_phone varchar(20), fax varchar(20), email varchar(70), notes varchar(500), primary key(id), foreign key(type_id) references contact_types(id))engine=InnoDB;	 
;;
;;  we need id because in bonds table there is ref to contact_id
;;	 where there is no contact just company
;;	
	 create table company_contacts(id int not null auto_increment, company_id int, contact_id int, foreign key(company_id) references companies(id),foreign key(contact_id) references contacts(id),primary key(id), unique unique_index(company_id, contact_id))engine=InnoDB;	 
;;
;;	 make the two columns unique
  alter table company_contacts add unique unique_index(company_id, contact_id);
	 
	 create table bond_companies(id int not null auto_increment, name varchar(70) not null unique, primary key(id))engine=InnoDB;
	 
	 create table bonds (id int not null auto_increment, bond_company_id, bond_num varchar(20), expire_date date, amount double(12,2), company_contact_id int, notes varchar(500), description varchar(70), type enum('Excavation','Development','Grading') default 'Excavation', description varchar(70), primary key(id), foreign key(bond_company_id) references bond_companies(id),foreign key(company_contact_id) references company_contacts(id))engine=InnoDB;

	 create table addresses(id int not null auto_increment,address varchar(70), loc_lat double(12,6), loc_long double(12,6), primary key(id))engine=InnoDB;

	 create table statuses(id int not null auto_increment, name varchar(30) not null unique, primary key(id))engine=InnoDB;
	 
	 insert into statuses values(1,'Not Started'),(2,'In Progress'),(3,'Complete'),(4,'Temporary Patch');
	 
	 create table utility_types(id int not null auto_increment, name varchar(30) not null unique, primary key(id))engine=InnoDB;
;; 
	 insert into utility_types values(1,'Electric'),(2,'Gas-Oil-Steam'),(3,'San-Sewer'),(4,'Storm'),(5,'Telecom'),(6,'Water'),(7,'Other');
;;
	 create table invoices(id int not null auto_increment, status enum('Billed','Pending','Paid'),date date, company_contact_id int, start_date date,end_date date, notes varchar(250), invoice_num varchar(10), primary key(id), foreign key(company_contact_id) references company_contacts(id))engine=InnoDB;
;;	
;;	 permits table was not possible so we prefixed with excav
;;
	 create table excavpermits(id int not null auto_increment, permit_num varchar(15), status enum('Not Started','Active','On Hold','Work Complete','Closed','Violation'),project varchar(70), reviewer_id int, start_date date, date date, fee double(12,2), bond_id int, company_contact_id int, notes varchar(1000), invoice_id int, permit_type enum('ROW','Grading'), primary key(id), foreign key(reviewer_id) references inspectors(user_id),foreign key(bond_id) references bonds(id),foreign key(company_contact_id) references company_contacts(id), foreign key(invoice_id) references invoices(id),index(permit_num))engine=InnoDB;
;;
	 create table cut_types(id int not null auto_increment, name varchar(70), primary key(id))engine=InnoDB;
;;
	 insert into cut_types values(1,'Street'),(2,'Sidewalk'),(3,'Bore'),(4,'TreePlot'),(10,'Other');
;;
;; the name excavations was not accepted by mysql so changed to excavcuts
;;
	 create table excavcuts(id int not null auto_increment, cut_description varchar(500), cut_type enum('Street','Sidewalk','Bore','TreePlot','Other'), permit_num varchar(15), address_id int, depth int, width int, length int, status enum('Not Started','In Progress','Complete','Temporary Patch'), utility_type_id int, primary key(id), foreign key(utility_type_id) references utility_types(id), foreign key(address_id) references addresses(id),index(permit_num))engine=InnoDB;
;;
;; we are using permit_id instead of permit in access
;; some permit numbers have .1 that is why we increased to varchar(14)
;;
	 create table inspections(id int not null auto_increment, inspector_id int, date date, permit_num varchar(15), notes varchar(1000), status enum('Not Started','In Progress','Need Touchup','Need Folowup','Completed'), has_picture char(1), followup_date date,  primary key(id), foreign key(inspector_id) references inspectors(user_id),index(permit_num))engine=InnoDB;
;;
	 create table permit_seq(id int not null auto_increment, permit_num int, primary key(id))engine=InnoDB;
;;
	 create table category_tables(id varchar(20) unique primary key, name varchar(30))engine=InnoDB;
	 insert into category_tables values('bond_types','Bond Types'),('bond_companies','Bond Companies'),('contact_types','Contact Types'),('cut_types','Cut Types'),('utility_types','Utility Types'),('statuses','Statuses');
;;
	 create table receipts(id int not null auto_increment, invoice_id int, date date, payment_type enum('Cash','Check','Money Order', 'Credit Card'), check_num varchar(70), paid_by varchar(70), user_id int, voided char(1), amount_paid double(10,2), primary key(id), foreign key(invoice_id) references invoices(id))engine=InnoDB;
  



















































