<template>
  <div>
    <highlightjs language="python" :code="log" />
  </div>
</template>
<script>
export default {
  async asyncData({ query, $axios }) {
    const log = await $axios.$get(`/api/logs?file=${query.file}`);
    return { log };
  },
};
</script>
