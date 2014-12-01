# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table favorito (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  usuario_id                bigint,
  constraint pk_favorito primary key (id))
;

create table telefono (
  id                        bigint not null,
  numero                    varchar(255),
  movil                     boolean,
  usuario_id                bigint,
  constraint pk_telefono primary key (id))
;

create table usuario (
  id                        bigint not null,
  nombre                    varchar(255),
  apellidos                 varchar(255),
  constraint pk_usuario primary key (id))
;

create sequence favorito_seq;

create sequence telefono_seq;

create sequence usuario_seq;

alter table favorito add constraint fk_favorito_usuario_1 foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_favorito_usuario_1 on favorito (usuario_id);
alter table telefono add constraint fk_telefono_usuario_2 foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_telefono_usuario_2 on telefono (usuario_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists favorito;

drop table if exists telefono;

drop table if exists usuario;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists favorito_seq;

drop sequence if exists telefono_seq;

drop sequence if exists usuario_seq;
