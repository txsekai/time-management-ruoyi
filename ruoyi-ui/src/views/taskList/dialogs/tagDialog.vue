<template>
  <el-dialog
    title="请选择标签"
    :visible.sync="tagDialogVisible"
    width="30%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-row>
      <el-tag
        class="button-tag user-select-none"
        :key="tag.vKey"
        v-for="tag in dynamicTags"
        closable
        @close="handleCloseTag(tag)"
        @click="handleSelectOrCancelTag(tag)"
        :class="{selected: isSelected(tag.vKey)}"
      >{{ tag.tagName }}
      </el-tag>
      <el-input
        class="input-new-tag"
        v-if="inputVisible"
        v-model="inputValue"
        ref="saveTagInput"
        size="small"
        @keyup.enter.native="handleInputConfirm"
        @blur="handleInputConfirm"
      ></el-input>
      <el-button v-else class="button-new-tag" @click="showInput" size="small">+ 新标签</el-button>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button class="buttonPadding" @click="handleConfirm">确认</el-button>
      <el-button class="buttonPadding" @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {addTag, delTag, deselectTagToTask, listTags, selectTagToTask} from "../../../api/taskList/tag";
import {v4 as uuidv4} from 'uuid';

export default {
  name: 'TagDialog',

  props: {
    tagDialogVisible: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object,
    },
    tagsBk: {
      type: Array,
      default: function () {
        return []
      }
    }
  },
  data() {
    return {
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      tempTags: [],
      tempSelectTagIds: [],
      tempDeselectTagIds: [],
    }
  },

  created() {
    this.getTagList();
  },

  computed: {

  },

  methods: {
    isSelected(tagVKey) {
      const tagIds = this.task.tags.map(tag => tag.tagId);
      return tagIds.includes(tagVKey) || this.tempSelectTagIds.includes(tagVKey);
    },
    getTagList() {
      listTags().then(res => {
        this.dynamicTags = res.data.map(item => ({vKey: item.tagId, tagId: item.tagId, tagName: item.tagName}));
      })
    },generateUniqueId() {
      return uuidv4();
    },
    handleInputConfirm() {
      const inputValue = this.inputValue
      if (inputValue) {
        if (this.dynamicTags.some(tag => tag.tagName === inputValue)) {
          this.$message({
            message: '标签已经存在',
            type: 'warning'
          })
        } else {
          // 临时加到dynamicTags里面
          const newTag = {
            vKey: this.generateUniqueId(),
            tagName: inputValue
          }
          this.dynamicTags.push(newTag);
          this.tempTags.push(newTag)
        }
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    showInput() {
      this.inputVisible = true
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleCloseTag(tag) {
      this.$confirm("确认要删除标签吗？", "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const tempIndex = this.tempTags.findIndex(tempTag => tempTag.vKey === tag.vKey);
        if(tempIndex !== -1) {
          this.tempTags.splice(tempIndex , 1);
          const dynamicIndex = this.dynamicTags.findIndex(dynamicTag => dynamicTag.vKey === tag.vKey);
          this.dynamicTags.splice(dynamicIndex, 1)
        }else {
          delTag(tag.tagId).then(res => {
            this.$modal.msgSuccess("删除成功")
            this.getTagList();
          })
        }

        // this.dynamicTags.splice(this.dynamicTags.indexOf(tag.tagId), 1)
        // if(this.task.tags.includes(tag.tagId)) {
        //   this.deselectTag(tag.tagId)
        // }
        //
        // if(this.tagsBk.includes(tag.tagId)) {
        //   const index = this.tagsBk.indexOf(tag.tagId)
        //   if (index !== -1) {
        //     this.tagsBk.splice(index, 1)
        //   }
        // }
      }).catch(() => {})
    },
    handleSelectOrCancelTag(tag) {
      if (this.isSelected(tag.vKey)) {
        this.deselectTag(tag.vKey)
      } else {
        this.selectTag(tag.vKey)
      }
    },
    selectTag(tagVKey) {
      const tagIds = this.task.tags.map(tag => tag.tagId);

      if (!tagIds.includes(tagVKey)) {
        this.tempSelectTagIds.push(tagVKey);
      }
    },
    deselectTag(tagVKey) {
      // 要取消选择的tag在临时tempSelectTagIds里面
      const index = this.tempSelectTagIds.indexOf(tagVKey);
      if(index !== -1) {
        this.tempSelectTagIds.splice(index, 1);
      }else {
        // 要取消选择的tag在task.tags里面(在数据库中)
        const tagIds = this.task.tags.map(tag => tag.tagId);
        if(tagIds.includes(tagVKey)) {
          this.tempDeselectTagIds.push(tagVKey);
        }
      }
    },
    handleConfirm() {
      debugger
      if (this.tempTags.length > 0) {
        addTag(this.tempTags).then(res => {
          this.$modal.msgSuccess("新增标签成功")
        })
      }

      if(this.tempSelectTagIds.length > 0) {
        selectTagToTask(this.task.taskId, this.tempSelectTagIds).then(res => {
          this.$modal.msgSuccess("选择标签成功")
        })
      }

      if(this.tempDeselectTagIds.length > 0) {
        deselectTagToTask(this.task.taskId, this.tempDeselectTagIds).then(res => {
          this.$modal.msgSuccess("取消选择标签成功")
        })
      }

      this.$emit("tagConfirm")
    },
    handleClose() {
      this.$emit("tagCancel")
    },
  },
}
</script>

<style scoped>
/deep/ .el-button {
  font-size: 1.4rem;
}

.el-tag {
  margin-left: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}

.selected {
  font-weight: bold;
}

.button-new-tag {
  margin-left: 1rem;
  height: 3.2rem;
  line-height: 3rem;
  padding-top: 0;
  padding-bottom: 0;
  margin-left: 1rem;
  margin-bottom: 1rem;
}

.input-new-tag {
  width: 9rem;
  margin-left: 1rem;
  margin-bottom: 1rem;
  vertical-align: bottom;
}
</style>
