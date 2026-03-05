<template>
  <div class="app-container">
    <!-- 上传区域 -->
    <el-card class="box-card" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>Excel上传建表</span>
      </div>
      <el-form :model="uploadForm" ref="uploadForm" size="small" label-width="100px">
        <el-form-item label="表描述" prop="tableComment">
          <el-input
            v-model="uploadForm.tableComment"
            placeholder="请输入表描述（可选，默认使用文件名）"
            clearable
            style="width: 400px"
          />
        </el-form-item>
        <el-form-item label="选择文件">
          <el-upload
            ref="upload"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="fileList"
            :limit="1"
            accept=".xlsx,.xls"
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">支持 .xlsx, .xls 格式，文件大小不超过 100MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            icon="el-icon-upload2" 
            :loading="uploadLoading"
            :disabled="!uploadFile"
            @click="handleUpload"
          >开始上传</el-button>
          <el-button icon="el-icon-refresh" @click="resetUpload">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 批次结果展示 -->
    <el-card v-if="uploadBatchResult && uploadBatchResult.sheetResults" class="box-card" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>批次结果</span>
      </div>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="总sheet">{{ uploadBatchResult.totalSheets }}</el-descriptions-item>
        <el-descriptions-item label="成功sheet">{{ uploadBatchResult.successSheets }}</el-descriptions-item>
        <el-descriptions-item label="失败sheet">{{ uploadBatchResult.failedSheets }}</el-descriptions-item>
        <el-descriptions-item label="总导入行数">{{ uploadBatchResult.totalRows }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">sheet明细</el-divider>
      <el-table :data="uploadBatchResult.sheetResults" border size="small">
        <el-table-column label="Sheet" min-width="140">
          <template slot-scope="scope">
            {{ scope.row.sheetNo + 1 }} - {{ scope.row.sheetName }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.success ? 'success' : 'danger'" size="small">
              {{ scope.row.success ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="结果说明" prop="message" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="表名" prop="tableName" min-width="160" :show-overflow-tooltip="true" />
        <el-table-column label="数据行数" prop="rowCount" width="100" align="center" />
      </el-table>
    </el-card>

    <!-- 首个成功sheet结果展示 -->
    <el-card v-if="uploadResult" class="box-card" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>表结构预览（首个成功sheet）</span>
        <el-tag v-if="uploadResult.newTable" type="success" size="small" style="margin-left: 10px;">新建表</el-tag>
        <el-tag v-else type="info" size="small" style="margin-left: 10px;">表已存在</el-tag>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="表名">{{ uploadResult.tableName }}</el-descriptions-item>
        <el-descriptions-item label="表描述">{{ uploadResult.tableComment }}</el-descriptions-item>
        <el-descriptions-item label="原始文件名">{{ uploadResult.fileName }}</el-descriptions-item>
        <el-descriptions-item label="数据行数">{{ uploadResult.rowCount }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider content-position="left">表结构</el-divider>
      <el-table :data="uploadResult.columns" border size="small">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="列名" prop="columnName" min-width="120" />
        <el-table-column label="列描述" prop="columnComment" min-width="150" />
        <el-table-column label="数据类型" prop="columnType" width="150" />
        <el-table-column label="Java类型" prop="javaType" width="120" />
      </el-table>
    </el-card>

    <!-- 列表 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>已上传的Excel表</span>
      </div>
      
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
        <el-form-item label="表名" prop="tableName">
          <el-input
            v-model="queryParams.tableName"
            placeholder="请输入表名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="表描述" prop="tableComment">
          <el-input
            v-model="queryParams.tableComment"
            placeholder="请输入表描述"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['ods:excel:remove']"
          >删除</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="excelTableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="表名" prop="tableName" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="表描述" prop="tableComment" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="原始文件名" prop="fileName" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="数据行数" prop="rowCount" width="100" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
              v-hasPermi="['ods:excel:query']"
            >查看</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-tickets"
              @click="handleViewData(scope.row)"
              v-hasPermi="['ods:excel:query']"
            >数据</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['ods:excel:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

  </div>
</template>

<script>
import { listExcelTable, uploadExcel, delExcelTable } from "@/api/ods/excel"

export default {
  name: "ExcelTable",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // Excel上传表表格数据
      excelTableList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: null,
        tableComment: null
      },
      // 上传表单
      uploadForm: {
        tableComment: null
      },
      // 文件列表
      fileList: [],
      // 当前选择的文件
      uploadFile: null,
      // 上传中
      uploadLoading: false,
      // 上传结果
      uploadResult: null,
      // 批次上传结果
      uploadBatchResult: null
    }
  },
  created() {
    this.getList()
  },
  activated() {
    if (this.$route.query && this.$route.query.t) {
      this.resetListState()
      this.$router.replace({ path: "/ods/excel" })
    }
  },
  methods: {
    /** 查询Excel上传表列表 */
    getList() {
      this.loading = true
      listExcelTable(this.queryParams).then(response => {
        this.excelTableList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 重置列表到默认状态
    resetListState() {
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.queryParams.tableName = null
      this.queryParams.tableComment = null
      this.ids = []
      this.single = true
      this.multiple = true
      this.getList()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tableId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 文件选择变化
    handleFileChange(file, fileList) {
      this.uploadFile = file.raw
      this.fileList = fileList
    },
    // 文件移除
    handleFileRemove(file, fileList) {
      this.uploadFile = null
      this.fileList = []
    },
    // 重置上传
    resetUpload() {
      this.uploadFile = null
      this.fileList = []
      this.uploadForm.tableComment = null
      this.uploadResult = null
      this.uploadBatchResult = null
      this.$refs.upload.clearFiles()
    },
    // 上传文件
    handleUpload() {
      if (!this.uploadFile) {
        this.$message.warning("请选择要上传的文件")
        return
      }

      // 检查文件类型
      const fileName = this.uploadFile.name
      const fileExt = fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
      if (fileExt !== '.xlsx' && fileExt !== '.xls') {
        this.$message.error("请上传 .xlsx 或 .xls 格式的文件")
        return
      }

      this.uploadLoading = true
      const formData = new FormData()
      formData.append('file', this.uploadFile)
      if (this.uploadForm.tableComment) {
        formData.append('tableComment', this.uploadForm.tableComment)
      }

      uploadExcel(formData).then(response => {
        this.uploadLoading = false
        if (response.code === 200) {
          this.uploadBatchResult = response.data
          if (response.data && response.data.sheetResults) {
            const successSheet = response.data.sheetResults.find(item => item.success)
            this.uploadResult = successSheet || null
          } else {
            this.uploadResult = response.data
          }
          this.$message.success(response.msg || "上传成功")
          this.getList()
        } else {
          this.$message.error(response.msg || "上传失败")
        }
      }).catch(error => {
        this.uploadLoading = false
        this.$message.error("上传失败：" + error.message)
      })
    },
    // 查看详情
    handleView(row) {
      this.$router.push({ path: "/ods/excel-detail/index/" + row.tableId })
    },
    // 查看数据
    handleViewData(row) {
      this.$router.push({ path: "/ods/excel-data/index/" + row.tableId })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tableIds = row.tableId || this.ids
      this.$confirm('是否确认删除选中的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delExcelTable(tableIds)
      }).then(() => {
        this.getList()
        this.$message.success("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
