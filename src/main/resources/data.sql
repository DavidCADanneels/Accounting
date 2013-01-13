insert into accountType (id, name) values (1, 'Active');
insert into accountType (id, name) values (2, 'Passive');
insert into accountType (id, name) values (3, 'Cost');
insert into accountType (id, name) values (4, 'Revenue');
insert into accountType (id, name) values (5, 'Credit');
insert into accountType (id, name) values (6, 'Debit');

insert into journalType (id, name) values (1, 'default');

insert into journalTypes (journalTypeId, debitAccountTypeId, creditAccountTypeId) values (1, 1, 2);
insert into journalTypes (journalTypeId, debitAccountTypeId, creditAccountTypeId) values (1, 3, 4);
insert into journalTypes (journalTypeId, debitAccountTypeId, creditAccountTypeId) values (1, 5, 6);
