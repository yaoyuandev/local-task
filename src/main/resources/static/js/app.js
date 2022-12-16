let editor;
htmx.onLoad((elt) => {
  new ClipboardJS(".btn");
});
htmx.on("textarea", "load", (elt) => {
  console.log(elt);
  editor = CodeMirror.fromTextArea(elt, {
    lineNumbers: true,
  });
  editor.on("change", () => {
    editor.save();
    console.log("save");
  });
});
