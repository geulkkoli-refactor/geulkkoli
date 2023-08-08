document.addEventListener("DOMContentLoaded", function () {
    const editorInstance = SUNEDITOR.create('editor_classic', {
        width: '100%',
        height: 'auto',
        buttonList: {}
    });
    document.querySelector(".sun-editor-editable").setAttribute("contenteditable", false);
    document.querySelector(".sun-editor-editable").ariaReadOnly = true;
});
