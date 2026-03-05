<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>Excel表详情</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          icon="el-icon-back"
          @click="handleBack"
        >返回列表</el-button>
      </div>

      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="表名">{{ detailData.tableName }}</el-descriptions-item>
        <el-descriptions-item label="表描述">{{ detailData.tableComment }}</el-descriptions-item>
        <el-descriptions-item label="原始文件名">{{ detailData.fileName }}</el-descriptions-item>
        <el-descriptions-item label="数据行数">{{ detailData.rowCount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">表结构详情</el-divider>
      <el-table v-loading="loading" :data="detailColumns" border size="small">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="列名" prop="columnName" min-width="120" />
        <el-table-column label="列描述" prop="columnComment" min-width="150" />
        <el-table-column label="数据类型" prop="columnType" width="150" />
        <el-table-column label="Java类型" prop="javaType" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getExcelTable } from "@/api/ods/excel"

export default {
  name: "ExcelDetail",
  data() {
    return {
      loading: false,
      tableId: null,
      detailData: null,
      detailColumns: []
    }
  },
  created() {
    this.tableId = this.$route.params && this.$route.params.tableId
    if (!this.tableId) {
      this.$message.error("缺少tableId参数")
      return
    }
    this.getDetail()
  },
  methods: {
    getDetail() {
      this.loading = true
      getExcelTable(this.tableId).then(response => {
        this.detailData = response.data
        this.detailColumns = (response.data && response.data.columns) || []
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error("获取表详情失败")
      })
    },
    handleBack() {
      this.$tab.closeOpenPage({ path: "/ods/excel", query: { t: Date.now() } })
    }
  }
}
</script>
