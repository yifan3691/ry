-- Excel上传表管理
CREATE TABLE excel_table (
    table_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    table_name VARCHAR(64) NOT NULL COMMENT '生成的表名(upload_xxx)',
    table_comment VARCHAR(200) COMMENT '表描述',
    structure_hash VARCHAR(64) NOT NULL COMMENT '结构MD5(列名+类型+顺序)',
    file_name VARCHAR(200) COMMENT '原始文件名',
    row_count INT DEFAULT 0 COMMENT '数据行数',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_structure (structure_hash),
    UNIQUE KEY uk_name (table_name)
) COMMENT='Excel上传表管理';

-- Excel表列信息
CREATE TABLE excel_table_column (
    column_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    table_id BIGINT NOT NULL COMMENT '关联excel_table',
    column_name VARCHAR(64) NOT NULL COMMENT '列名(英文)',
    column_comment VARCHAR(200) COMMENT '列描述(Excel表头)',
    column_type VARCHAR(50) NOT NULL COMMENT 'MySQL类型',
    java_type VARCHAR(20) COMMENT 'Java类型',
    sort INT NOT NULL COMMENT '排序',
    is_pk CHAR(1) DEFAULT '0' COMMENT '是否主键',
    is_required CHAR(1) DEFAULT '0' COMMENT '是否必填',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='Excel表列信息';
