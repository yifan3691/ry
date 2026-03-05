<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ pageTitle }}</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          icon="el-icon-back"
          @click="handleBack"
        >返回列表</el-button>
      </div>

      <el-table v-loading="dataLoading" :data="tableDataList" border size="small">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column
          v-for="col in dataColumns"
          :key="col.columnName"
          :prop="col.columnName"
          :label="col.columnComment || col.columnName"
          min-width="120"
          :show-overflow-tooltip="true"
        />
      </el-table>

      <pagination
        v-show="dataTotal > 0"
        :total="dataTotal"
        :page.sync="dataQueryParams.pageNum"
        :limit.sync="dataQueryParams.pageSize"
        @pagination="getTableDataList"
      />
    </el-card>
  </div>
</template>

<script>
import { getExcelTable, getTableData } from "@/api/ods/excel"

export default {
  name: "ExcelData",
  data() {
    return {
      tableId: null,
      tableInfo: null,
      dataLoading: false,
      dataColumns: [],
      tableDataList: [],
      dataTotal: 0,
      dataQueryParams: {
        pageNum: 1,
        pageSize: 20
      }
    }
  },
  computed: {
    pageTitle() {
      if (this.tableInfo && this.tableInfo.tableComment) {
        return this.tableInfo.tableComment + " - 数据预览"
      }
      return "数据预览"
    }
  },
  created() {
    this.tableId = this.$route.params && this.$route.params.tableId
    if (!this.tableId) {
      this.$message.error("缺少tableId参数")
      return
    }

    getExcelTable(this.tableId).then(response => {
      this.tableInfo = response.data
      this.dataColumns = (response.data && response.data.columns) || []
      this.getTableDataList()
    }).catch(() => {
      this.$message.error("获取表结构失败")
    })
  },
  methods: {
    getTableDataList() {
      this.dataLoading = true
      getTableData(this.tableId, this.dataQueryParams).then(response => {
        this.tableDataList = response.rows || []
        this.dataTotal = response.total || 0
        this.dataLoading = false
      }).catch(() => {
        this.dataLoading = false
        this.$message.error("获取表数据失败")
      })
    },
    handleBack() {
      this.$tab.closeOpenPage({ path: "/ods/excel", query: { t: Date.now() } })
    }
  }
}
</script>
