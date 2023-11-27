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
        :key="tag.tagId"
        v-for="tag in dynamicTags"
        closable
        @close="handleCloseTag(tag.tagId)"
        @click="handleSelectOrCancelTag(tag.tagId)"
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
import {addTag, listTags} from "../../../api/taskList/tag";
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'TagDialog',

  props: {
    tagDialogVisible: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object,
      default: function() {
        return {tags:[]}
      }
    },
    tagsBk: {
      type: Array,
      default: function (){
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
    }
  },

  created() {
    this.initTagList();
  },

  computed: {
    isSelected() {
      return tag => this.task.tags.includes(tag)
    },
  },

  methods: {
    initTagList() {
      listTags().then(res => {
        this.dynamicTags = res.data.map(item => ({ tagId: item.tagId, tagName: item.tagName }));
      })
    },
    handleCloseTag(tag) {
      this.$confirm("确认要删除标签吗？", "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
        if(this.task.tags.includes(tag)) {
          this.deselectTag(tag)
        }

        if(this.tagsBk.includes(tag)) {
          const index = this.tagsBk.indexOf(tag)
          if (index !== -1) {
            this.tagsBk.splice(index, 1)
          }
        }
      }).catch(() => {})
    },
    handleSelectOrCancelTag(tag) {
      if (this.isSelected(tag)) {
        this.deselectTag(tag)
      } else {
        this.selectTag(tag)
      }
    },
    selectTag(tag) {
      if(!this.task.tags.includes(tag)){
        this.task.tags.push(tag);
      }
    },
    deselectTag(tag) {
      const index = this.task.tags.indexOf(tag)
      if (index !== -1) {
        this.task.tags.splice(index, 1)
      }
    },
    generateUniqueId() {
      return uuidv4();
    },
    handleInputConfirm() {
      const inputValue = this.inputValue
      if (inputValue) {
        if(this.dynamicTags.some(tag => tag.tagName === inputValue)){
          this.$message({
            message: '标签已经存在',
            type: 'warning'
          })
        }else {
          // 临时加到dynamicTags里面
          const newTag = {
            tagId: this.generateUniqueId(),
            tagName: inputValue
          }
          this.dynamicTags.push(newTag);
          this.tempTags.push(inputValue)
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
      addTag(this.tempTags).then(res => {
        this.$modal.msgSuccess("新增标签成功")
      })
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
