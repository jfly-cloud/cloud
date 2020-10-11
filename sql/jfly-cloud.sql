-- ----------------------------
-- 1、oauth_client_details
-- http://localhost:8080/oauth/authorize?response_type=code&grant_type=authorization_code&client_id=jfly-cloud&client_secret=f9638dcb2fe95c5585f585bedd1f6535&redirect_uri=http://localhost:8080/login
-- http://localhost:8080/oauth/authorize?response_type=token&grant_type=implicit&client_id=jfly-cloud&client_secret=f9638dcb2fe95c5585f585bedd1f6535
-- ----------------------------
drop table if exists oauth_client_details;
create table oauth_client_details (
  client_id VARCHAR(128) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);
INSERT INTO `oauth_client_details` VALUES ('jfly-cloud', 'ids', 'f9638dcb2fe95c5585f585bedd1f6535', 'all', 'implicit,password,client_credentials,authorization_code,refresh_token', 'http://localhost:8080/login', '', 3600, 3600, '{\"country\":\"CN\",\"country_code\":\"086\"}', 'false');
-- ----------------------------
-- 2、jfly_user
-- ----------------------------
drop table if exists jfly_user;
create table jfly_user (
  id                bigint(20)      not null                    comment '用户id',
  user_name         varchar(30)     not null                    comment '用户名',
  password          varchar(100)    not null                    comment '密码',
  nick_name         varchar(30)     default null                comment '用户昵称',
  mobile            varchar(50)     default null                comment '手机号码:国际长途区号—手机号码',
  email             varchar(50)     default null                comment '用户邮箱',
  sex               char(1)         default '1'                 comment '用户性别（1男 2女 0未知）',
  avatar            varchar(100)    default null                comment '头像路径',
  channel           varchar(50)     not null                    comment '渠道,WEB/APP(精确至分发渠道)/小程序',
  status            char(1)         default '1'                 comment '帐号状态（1正常 0停用/拉黑）',
  login_ip          varchar(64)     default null                comment '最后登陆IP',
  login_date        datetime                                    comment '最后登陆时间',
  deleted           char(1)         default '0'                 comment '删除标志（0代表存在 1代表删除）',
  created_by         varchar(64)     default null               comment '创建者',
  created_time       datetime                                   comment '创建时间',
  updated_by         varchar(64)     default null               comment '更新者',
  updated_time       datetime                                   comment '更新时间',
  remark           varchar(500)      default null               comment '备注',
  primary key (`id`),
  unique key `user_name` (`user_name`),
  unique key `mobile` (`mobile`),
  unique key `email` (`email`)
) engine=innodb  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci comment = '会员信息表';
INSERT INTO jfly_user VALUES ('1','admin','21232f297a57a5a743894a0e4a801fc3','test','86-13905318174','test@163.com','1','oss','WEB','1','192.168.1.23','2020-07-12 07:56:12.0','0','system','2020-07-12 07:56:12.0','system','2020-07-12 07:56:12.0','2020-07-12 07:56:12.0');