<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand to="/">Local Task 2.0</b-navbar-brand>
      <b-navbar-nav>
        <b-nav-item>Templates</b-nav-item>
      </b-navbar-nav>
    </b-navbar>
    <b-table :items="items" :fields="fields">
      <template #cell(use)="data">
        <b-button variant="light">
          <NuxtLink :to="`/?templateId=${data.item.id}`">use</NuxtLink>
        </b-button>
      </template>
    </b-table>
  </div>
</template>
<script>
export default {
  async asyncData({ $axios }) {
    const items = (await $axios.get("/api/templates")).data;
    return { items };
  },
  data() {
    return {
      items: [],
      fields: [
        { key: "id", label: "#" },
        { key: "interpreter", label: "interpreter" },
        { key: "cmd", label: "cmd" },
        { key: "createdAt", label: "createdAt" },
        { key: "updatedAt", label: "updatedAt" },
        { key: "use", label: "use" },
      ],
    };
  },
  methods: {
    use(id) {
      console.log(id);
    },
  },
};
</script>
