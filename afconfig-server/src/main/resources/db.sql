create table if not exists `config` (
    `app` varchar(64) not null,
    `env` varchar(64) not null,
    `ns` varchar(64) not null,
    `pkey` varchar(64) not null,
    `pval` varchar(128) null
);

insert into config values ('app1', 'dev', 'public', 'af.a', '1.0.0');
insert into config values ('app1', 'dev', 'public', 'af.b', '2.0.0');
insert into config values ('app1', 'dev', 'public', 'af.c', '3.0.0');
