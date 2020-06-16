# 创建一个用户管理的数据源
CREATE DATABASE IF NOT EXISTS user_auth DEFAULT CHARACTER SET = utf8;

USE user_auth;

CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `username` varchar(32) NOT NULL UNIQUE COMMENT '用户名',
  `password` varchar(32) NULL DEFAULT '' COMMENT '用户密码',
  `locked` bit(1) NOT NULL DEFAULT b'0' COMMENT '0: locked, 0: normal',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建的时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户修改的时间',
  PRIMARY KEY (`id`),
  UNIQUE user_name_index (`username`),
  KEY create_time_index (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户基本信息表';

CREATE TABLE IF NOT EXISTS `role` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `rolename` varchar(32) NOT NULL UNIQUE COMMENT '角色名',
  `des` TINYTEXT NULL COMMENT '描述信息',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建的时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改的时间',
  PRIMARY KEY (`id`),
  KEY (`rolename`),
  KEY (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色基本信息表';

CREATE TABLE IF NOT EXISTS `api` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `url` varchar(32) NOT NULL UNIQUE COMMENT '访问的路径 全路径',
  `name` TINYTEXT NOT NULL COMMENT '访问路径的名称',
  `des` TINYTEXT NULL COMMENT '描述信息',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建的时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改的时间',
  PRIMARY KEY (`id`),
  KEY (`name`(64)),
  KEY (`url`),
  KEY (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口基本信息表';

CREATE TABLE IF NOT EXISTS `user_role_relate` (
  `user_id` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(32) NOT NULL COMMENT '主键',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改的时间',
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT fk_ur_1 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_ur_2 FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS `role_api_relate` (
  `user_id` varchar(32) NOT NULL COMMENT '主键',
  `api_id` varchar(32) NOT NULL COMMENT '主键',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改的时间',
  PRIMARY KEY (`user_id`, `api_id`),
  CONSTRAINT fk_ra_1 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_ra_2 FOREIGN KEY (`api_id`) REFERENCES `api`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色接口关联表';

CREATE TABLE IF NOT EXISTS `user_op_trace` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `op` TINYINT NOT NULL COMMENT '0 添加, 1 删除, 2修改, 3访问',
  `table_name` varchar(32) NULL DEFAULT '' COMMENT '操作的表名(数据库)',
  `table_name_detail` varchar(32) NULL DEFAULT '' COMMENT '操作的表名',
  `sql_content` TINYTEXT NULL COMMENT '操作的sql语句',
  `des` TINYTEXT NULL COMMENT '操作的描述',
  `result` bit(1) NOT NULL DEFAULT b'0' COMMENT '0: 操作失败, 1: 操作成功',
  `user_id` varchar(32) NOT NULL COMMENT '用户的id',
  `ip` varchar(16) NULL DEFAULT '127.0.0.1' COMMENT '操作地点',
  `op_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作的时间',
  PRIMARY KEY (`id`),
  KEY (`op`),
  KEY (`result`),
  KEY (`op_time`),
  KEY (`table_name_detail`),
  KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录';

CREATE TABLE IF NOT EXISTS `user_op_trace_bak` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `op` TINYINT NOT NULL COMMENT '0 添加, 1 删除, 2修改, 3访问',
  `table_name` varchar(32) NULL DEFAULT '' COMMENT '操作的表名(数据库)',
  `table_name_detail` varchar(32) NULL DEFAULT '' COMMENT '操作的表名',
  `sql_content` TINYTEXT NULL COMMENT '操作的sql语句',
  `des` TINYTEXT NULL COMMENT '操作的描述',
  `result` bit(1) NOT NULL DEFAULT b'0' COMMENT '0: 操作失败, 1: 操作成功',
  `user_id` varchar(32) NOT NULL COMMENT '用户的id',
  `ip` varchar(16) NULL DEFAULT '127.0.0.1' COMMENT '操作地点',
  `op_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作的时间',
  PRIMARY KEY (`id`),
  KEY (`op`),
  KEY (`result`),
  KEY (`op_time`),
  KEY (`table_name_detail`),
  KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录';

# 删除定时事件
DROP EVENT IF EXISTS bak_op_trace_event;
# 删除存储过程
DROP PROCEDURE IF EXISTS bak_op_trace;

# 创建存储过程
DELIMITER //
CREATE PROCEDURE bak_op_trace()
BEGIN
	INSERT INTO user_op_trace_bak SELECT * FROM user_op_trace now WHERE now.op_time < DATE_SUB(CURDATE(), INTERVAL 1 DAY);
	DELETE FROM user_op_trace WHERE op_time < DATE_SUB(CURDATE(), INTERVAL 1 DAY);
	INSERT INTO user_op_trace (id, op, table_name, table_name_detail, sql_content, des, result, user_id)
	VALUES (REPLACE(UUID(), '-', ''), 0, 'user_op_trace user_op_trace_bak', '操作记录表', '存储过程(bak_op_trace)执行', '触发定时执行存储过程进行数据备份', 1, 'mysql');
END //
DELIMITER;

# 查看存储过程
# show CREATE procedure bak_op_trace;

# 创建事件并开始
# on schedule EVERY 1 second STARTS now() 按秒执行
# on schedule EVERY 1 day STARTS now() 按天执行
SET GLOBAL event_scheduler = ON;
CREATE event bak_op_trace_event
on schedule EVERY 1 day STARTS now()
ON COMPLETION NOT PRESERVE ENABLE
do call bak_op_trace();

# 查看事件状态
# select @@event_scheduler;
# 查看指定事件
# show CREATE event bak_op_trace_event;
