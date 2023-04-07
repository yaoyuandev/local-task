<template>
  <div>
    <highlightjs language="python" :code="log" />
  </div>
</template>
<script>
export default {
  data() {
    return {
      log: "",
    };
  },
  mounted() {
    this.startInterval();
  },
  methods: {
    async getLogs() {
      this.log = (
        await this.$axios.get(`/api/logs?file=${this.$route.query.file}`)
      ).data;
    },
    startInterval() {
      setInterval(async () => {
        await this.getLogs();
      }, 1000);
    },
  },
};
</script>
