import Vue from "vue";

import hljs from "highlight.js/lib/core";
import javascript from "highlight.js/lib/languages/javascript";
import markdown from "highlight.js/lib/languages/markdown";
import shell from "highlight.js/lib/languages/shell";
import python from "highlight.js/lib/languages/python";
import vuePlugin from "@highlightjs/vue-plugin";
import "highlight.js/styles/github.css";

hljs.registerLanguage("javascript", javascript);
hljs.registerLanguage("markdown", markdown);
hljs.registerLanguage("shell", shell);
hljs.registerLanguage("python", python);

Vue.use(vuePlugin);
