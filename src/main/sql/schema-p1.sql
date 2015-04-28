ALTER TABLE service MODIFY COLUMN ukey varchar(40) NOT NULL;

DROP TABLE IF EXISTS `doc_link`;
CREATE TABLE `doc_link` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `service_id` int(10) unsigned NOT NULL,
  `title` varchar(250) NOT NULL,
  `href` varchar(250) NOT NULL,
  `description` varchar(250) NOT NULL,
  `source_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_id_title` (`service_id`,`title`),
  KEY `source_id` (`source_id`),
  CONSTRAINT `doc_link_service_id` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  CONSTRAINT `doc_link_source_id` FOREIGN KEY (`source_id`) REFERENCES `source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Issue #94: NOT NULL constraint on node.service_instance_id
alter table node drop foreign key node_service_instance_id;
alter table node modify column service_instance_id int(10) unsigned not null;
alter table node add constraint node_service_instance_id foreign key (service_instance_id) references service_instance (id);

-- Issue #104: Message of the Day
drop table if exists conf_prop;
create table conf_prop (
  id int(10) unsigned not null auto_increment,
  `pkey` varchar(80) not null,
  `pvalue` text(4096),
  primary key (id),
  unique key `pkey` (`pkey`)
) engine=InnoDB default charset=utf8;
