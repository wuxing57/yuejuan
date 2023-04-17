<template>
    <div ref="wang"></div>
</template>

<script>
import wangEditor from 'wangeditor'

export default {
    props: {
        // wangEditor 的配置
        option: {
            type: Object,
            default() {
                return {}
            }
        },
        // v-model 双向绑定：接受父组件的值
        // https://cn.vuejs.org/v2/guide/components-custom-events.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BB%84%E4%BB%B6%E7%9A%84-v-model
        value: {
            type: String,
            default: ''
        }
    },
    watch: {
        // 监听父组件传值，将值赋值给编辑器
        value(n) {
            if (this.editor && n !== this.temp) {
                this.editor.$textElem.html(n)
            }
        }
    },
    data() {
        return {
            temp: '',   // 缓存当前的编辑器中的值，用于与父组件传入值进行对比
            editor: null
        }
    },
    mounted() {
        this.editor = new wangEditor(this.$refs.wang)

        // 合并配置项
        Object.assign(this.editor.config, this.option)

        // v-model 双向绑定：把值发送到父组件（不占用用户的 onchange 配置）
        this.editor.txt.eventHooks.changeEvents.push(() => {
            this.temp = this.editor.$textElem.html()
            this.$emit('input', this.temp)
        })

        this.editor.create()

        if (this.value.length) {
            this.editor.txt.html(this.value)
        }
    },
    beforeDestroy() {
        if (this.editor) {
            this.editor.destroy()
        }
    }
}
</script>