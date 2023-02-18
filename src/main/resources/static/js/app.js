htmx.onLoad((elt) => {
  new ClipboardJS(".btn");
  hljs.highlightAll();
});
var myCodeMirror = CodeMirror.fromTextArea(document.getElementById("cmd"), {
  lineNumbers: true,
  lineWrapping: true,
});
function clone(id) {
  const cmd = document.getElementById("task_" + id).innerText;
  const interpreter = document.getElementById(
    "task_interpreter_" + id
  ).innerText;
  myCodeMirror.setValue(cmd);
  const select = document.getElementById("interpreter");
  select.value = interpreter;
  select.dispatchEvent(new Event("change"));
}
