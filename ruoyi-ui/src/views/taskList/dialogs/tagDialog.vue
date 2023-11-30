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
        class="button-tag"
        :key="tag.vKey"
        v-for="tag in dynamicTags"
        closable
        @close="handleCloseTag(tag)"
        @click="handleSelectOrCancelTag(tag)"
        :class="{selected: isSelected(tag.tagId)}"
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
      inputValue: [],
      tempTags: [],
      tempSelectTagIds: [],
      tempDeselectTagIds: [],
    }
  },

  created() {
    this.getTagList();
  },

  computed: {
    isSelected() {
      // return tag => this.task.tags.includes(tag)
      const tagIds = this.task.tags.map(tag => tag.tagId);
      return tagId => tagIds.includes(tagId) || this.tempSelectTagIds.includes(tagId);
    },
  },

  methods: {
    getTagList() {
      listTags().then(res => {
        this.dynamicTags = res.data.map(item => ({vKey: item.tagId, tagId: item.tagId, tagName: item.tagName}));
      })
    },
    handleCloseTag(tag) {
      // this.$modal.confirm('是否确认删除标签: "' + tag.tagName + '"?')
      this.$modal.confirm('是否确认删除标签: ' + tag.tagName + '?').then(function () {
        return delTag(tag.tagId);
      }).then(() => {
        this.getTagList();
        this.$modal.msgSuccess("删除成功");

        //   TODO 查看删除的是否已选择,选择的话要删除关联
      }).catch(() => {
      });


      // this.$confirm("确认要删除标签吗？", "确认", {
      //   confirmButtonText: "确认",
      //   cancelButtonText: "取消",
      //   type: "warning"
      // }).then(() => {
      //   delTag(tag.tagId).then(res => {
      //     this.$modal.msgSuccess("删除成功")
      //   })
      //
      //   this.dynamicTags.splice(this.dynamicTags.indexOf(tag.tagId), 1)
      //   if(this.task.tags.includes(tag.tagId)) {
      //     this.deselectTag(tag.tagId)
      //   }
      //
      //   if(this.tagsBk.includes(tag.tagId)) {
      //     const index = this.tagsBk.indexOf(tag.tagId)
      //     if (index !== -1) {
      //       this.tagsBk.splice(index, 1)
      //     }
      //   }
      // }).catch(() => {})
    },
    handleSelectOrCancelTag(tag) {
      debugger
      if (this.isSelected(tag.tagId)) {
        this.deselectTag(tag.tagId)
      } else {
        this.selectTag(tag.tagId)
      }
    },
    selectTag(tagId) {
      debugger
      const tagIds = this.task.tags.map(tag => tag.tagId);

      if (!tagIds.includes(tagId)) {
        // this.task.tags.push(tag);
        this.tempSelectTagIds.push(tagId);
      }
    },
    deselectTag(tagId) {
      // const index = this.task.tags.indexOf(tag)
      // if (index !== -1) {
      //   this.task.tags.splice(index, 1)
      // }


      // 要取消选择的tag在临时tempSelectTagIds里面
      const index = this.tempSelectTagIds.indexOf(tagId);
      if(index !== -1) {
        this.tempSelectTagIds.splice(index, 1);
      }else {
        // 要取消选择的tag在task.tags里面(在数据库中)
        const tagIds = this.task.tags.map(tag => tag.tagId);
        if(tagIds.includes(tagId)) {
          this.tempDeselectTagIds.push(tagId);
        }
      }
    },
    generateUniqueId() {
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
    handleConfirm() {
      if (this.tempTags.length > 0) {
        addTag(this.tempTags).then(res => {
          this.$modal.msgSuccess("新增标签成功")
        })
      }
      debugger
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
