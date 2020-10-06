function fileValidation () {

    const fi = document.getElementById('check');
    let fsize = 0

    if (fi.files.length > 0) {
        for (let i = 0; i <= fi.files.length - 1; i++) {
            fsize += fi.files.item(i).size;
        }

        const file = Math.round((fsize / 1024));
        if (file >= 20480) {
            if (fi.files.length == 1)
                alert("Size of file is " + Math.round(file / 1024 * 100) / 100 + " MB. Please select a file less than 20mb");
            else alert("Size of files is " + Math.round(file / 1024 * 100) / 100 + " MB. Please select files less than 20mb");
            document.getElementById('check').value = null;
        }
    }
}