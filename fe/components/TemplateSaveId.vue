<template>
  <div v-b-hover="handleHover">
    <b-button v-text="taskId" v-if="!hovered" variant="link" />
    <b-button v-else @click="save(taskId)" variant="primary">Save</b-button>
  </div>
</template>
<script>
export default {
  props: ["taskId", "save"],
  data() {
    return {
      hovered: false,
    };
  },
  methods: {
    handleHover(hovered) {
      console.log("call");
      this.hovered = hovered;
    },
  },
};
</script>
