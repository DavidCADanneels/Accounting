create table accountType(
	id int not null auto_increment,
	name varchar(20) not null,
	primary key (id)
);

create table journalType(
	id int not null auto_increment,
	name varchar(20) not null,
	primary key (id)
);

create table journalTypes(
	id int not null auto_increment,
	journalTypeId int not null,
	debitAccountTypeId int,
	creditAccountTypeId int,
	accountingId int not null,
	primary key (id),
	foreign key accountingId references accounting(id),
	foreign key (journalTypeId) references journalType(id),
	foreign key (debitAccountTypeId) references accountType(id),
	foreign key (creditAccountTypeId) references accountType(id)
);

create table journal(
	id int not null auto_increment,
	journalTypeId int not null,
	name varchar(20) not null,
	abbreviation varchar(20) not null,
	-- id ???
	-- stylesheet (xsl)
	-- transactions (reverse link)
	accountingId int not null,
	primary key (id),
	foreign key accountingId references accounting(id),
	foreign key journalTypeId references journalType(id)
);

create table account (
	id int not null auto_increment,
	name varchar(20) not null,
	accountType int not null,
	debetTotal bigDecimal,
	creditTotal bigDecimal,
	-- links to files: xml, html, xsl
	-- links to project and accounting
	-- link to bookings (reverse link)
	accountingId int not null,
	primary key (id),
	foreign key accountingId references accounting(id),
	foreign key accountType references accountType(id)
);

create table booking(
	id int not null auto_increment,
	abbreviation varchar(20) not null,
	description varchar(20) not null,
	date date not null,
	account int not null,
	foreign key (account) references account(id),
	amount varchar(20) not null,
	debet int not null,
	primary key (id)
	-- link to transaction
);

create table transaction(
	id int not null auto_increment,
--	abbreviation varchar(20) not null,
	description varchar(20) not null,
	date date not null,
	primary key (id)	
);

create table transactions(
	id int not null auto_increment,
	transactionId int not null,
	bookingId int not null,
--  debitTotal,
--  creditTotal,
	journalId int not null,
	primary key (id),
	foreign key (transactionId) references transaction(id),
	foreign key (bookingId) references booking(id)
	foreign key (journalId) references journal(id)
);

create table project(
	id int not null auto_increment,
	name varchar(20),
	accountingId int not null,
	primary key (id),
	foreign key accountingId references accounting(id)
);

create table projects(
	id int not null auto_increment,
	projectId int not null,
	accountId int not null,
	primary key (id),
	foreign key (projectId) references project(id),
	foreign key (accountId) references account(id)	
);

create table accounting(
	id int not null auto_increment,
	name varchar(20),
	primary key (id)
);