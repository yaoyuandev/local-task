<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand to="/">Local Task 2.0</b-navbar-brand>
      <b-navbar-nav>
        <b-nav-item to="/task-template">Templates</b-nav-item>
      </b-navbar-nav>
      <b-nav-form class="ml-auto">
        <b-form-input placeholder="Search" v-model="queryInput"></b-form-input>
        <b-button type="submit" @click="search" variant="primary">
          <b-icon icon="search" />
        </b-button>
        <b-button
          v-if="onSearch"
          type="submit"
          @click="cancelSearch"
          variant="warning"
        >
          <b-icon icon="zoom-out" />
        </b-button>
      </b-nav-form>
    </b-navbar>
    <b-form>
      <b-row>
        <b-col>
          <b-input-group prepend="name">
            <b-form-input v-model="name"></b-form-input>
          </b-input-group>
        </b-col>
        <b-col md="1">
          <b-button @click="run({ name, cmd, interpreter })" variant="primary"
            >RUN</b-button
          >
        </b-col>
      </b-row>
      <b-form-select v-model="interpreter" :options="options"></b-form-select>
    </b-form>
    <codemirror v-model="cmd" :options="cmOptions"></codemirror>
    <b-table
      :items="items"
      :fields="fields"
      :per-page="perPage"
      :currentPage="currentPage"
      id="my-table"
    >
      <template #cell(id)="data">
        <template-save-id
          :task-id="data.item.id"
          :save="handleSave"
          style="width: 5em"
        ></template-save-id>
      </template>
      <template #cell(cmd)="data">
        <highlightjs
          language="python"
          :code="data.item.cmd"
          v-b-tooltip.hover
          :title="tip(data.item)"
        />
      </template>
      <template #cell(copy)="data">
        <b-button v-clipboard:copy="data.item.cmd" variant="link">
          <b-icon icon="clipboard"></b-icon>
        </b-button>
      </template>
      <template #cell(delete)="data">
        <b-button
          v-if="data.item.created"
          @click="cancelItem(data.item.id)"
          variant="outline-danger"
        >
          <b-icon icon="slash-circle" />
        </b-button>
        <b-button
          v-if="data.item.isRunning"
          @click="killItem(data.item.id)"
          variant="outline-danger"
        >
          <b-icon icon="stop-circle" />
        </b-button>
        <b-button
          v-if="!data.item.isRunning && !data.item.created"
          @click="deleteItem(data.item.id)"
          variant="outline-danger"
        >
          <b-icon icon="x-square" />
        </b-button>
      </template>
      <template #cell(retry)="data">
        <b-button @click="retryItem(data.item.id)" variant="outline-primary">
          <b-icon icon="arrow-repeat" />
        </b-button>
      </template>
      <template #cell(clone)="data">
        <b-button @click="cloneItem(data.item)" variant="outline-info">
          <b-icon icon="box-arrow-up-left" />
        </b-button>
      </template>
      <template #cell(output)="data">
        <a :href="data.item.outputUrl" target="_blank">{{
          data.item.output
        }}</a>
      </template>
      <template #cell(status)="data">
        <b-badge :variant="data.item.badge">{{ data.item.status }}</b-badge>
      </template>
    </b-table>
    <b-pagination
      v-model="currentPage"
      :total-rows="rows"
      :per-page="perPage"
      aria-controls="my-table"
    ></b-pagination>
  </div>
</template>

<script>
import { codemirror } from "vue-codemirror";
import "codemirror/lib/codemirror.css";

export default {
  name: "Index",
  async asyncData({ query, $axios }) {
    if ("templateId" in query) {
      const template = (await $axios.get(`/api/templates/${query.templateId}`))
        .data;
      return {
        cmd: template.cmd,
        interpreter: template.interpreter,
      };
    }
  },
  mounted() {
    this.startInterval();
  },
  data() {
    const currentPage = 1;
    const perPage = 5;
    const name = "";
    const options = ["bash", "ipython", "jupyter"];
    const cmOptions = {
      lineNumbers: true,
      line: true,
    };
    const items = [];
    const fields = [
      {
        key: "id",
        label: "#",
      },
      { key: "cmd" },
      { key: "copy" },
      { key: "output" },
      { key: "status" },
      { key: "duration" },
      { key: "startedAt" },
      { key: "delete", label: "Stop/Delete" },
      { key: "retry" },
      { key: "clone" },
    ];
    const counter = 0;
    const query = "";
    const onSearch = false;
    const queryInput = "";
    return {
      name,
      cmd: "",
      cmOptions,
      items,
      fields,
      counter,
      interpreter: "bash",
      options,
      currentPage,
      perPage,
      query,
      onSearch,
      queryInput,
      hovered: false,
    };
  },
  components: {
    codemirror,
  },
  methods: {
    async refreshItems() {
      if (this.onSearch) {
        this.items = (
          await this.$axios.get(`/api/tasks/search?q=${this.query}`)
        ).data.items;
        return;
      }
      this.items = (await this.$axios.get("/api/tasks")).data.items;
    },
    startInterval() {
      this.interval = setInterval(async () => {
        await this.refreshItems();
      }, 1000);
    },
    keyStr(key, value) {
      if (value !== "") {
        return key + "=" + value;
      }
      return "";
    },
    tip(item) {
      return (
        this.keyStr("pid", item.pid) +
        "\n" +
        this.keyStr("name", item.name) +
        "\n" +
        this.keyStr("interpreter", item.interpreter)
      );
    },
    async deleteItem(id) {
      await this.$axios.delete(`/api/tasks/${id}`);
      await this.refreshItems();
    },
    async run(task) {
      await this.$axios.post("/api/tasks", task);
      await this.refreshItems();
      await this.$bvToast.toast("run a task successfully", {
        variant: "success",
        autoHideDelay: 1000,
      });
    },
    async killItem(id) {
      await this.$axios.post(`/api/tasks/${id}/kill`);
      await this.refreshItems();
    },
    async retryItem(id) {
      await this.$axios.post(`/api/tasks/${id}/retry`);
      await this.refreshItems();
    },
    async search() {
      this.onSearch = true;
      this.query = this.queryInput;
      await this.refreshItems();
    },
    cloneItem(item) {
      this.name = item.name;
      this.cmd = item.cmd;
      this.interpreter = item.interpreter;
    },
    async cancelItem(id) {
      await this.$axios.post(`/api/tasks/${id}/cancel`);
      await this.refreshItems();
    },
    async cancelSearch() {
      this.onSearch = false;
      this.queryInput = "";
      await this.refreshItems();
    },
    async handleSave(taskId) {
      await this.$axios.post(`/api/templates?taskId=${taskId}`);
    },
  },
  computed: {
    rows() {
      return this.items.length;
    },
  },
};
</script>
