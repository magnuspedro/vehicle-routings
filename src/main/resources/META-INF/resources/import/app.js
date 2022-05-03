const button = $("#importButton")


let message = ""

const handleFileSelect = (evt) => {
    const files = evt.target.files;

    for (let i = 0, f; f = files[i]; i++) {
        const reader = new FileReader();
        reader.onload = ((theFile) => {
            return (e) => {
                message = e.target.result
            };
        })(f);

        reader.readAsText(f);
    }
}

const importFile = (body) => {
    jQuery.support.cors = true;
    $.ajax({
        type: "POST",
        url: "https://readble.herokuapp.com/api/v1/import",
        crossDomain: true,
        data: body,
        dataType: "text",
        success: data => {
            alert("Arquivo importado com sucesso")
        },
        error: data => {
            console.error(data)
            alert("Erro importando aquivo")
        }
    })
}

document.getElementById('importCsv').addEventListener('change', handleFileSelect, false);

const readFile = () => {
    if (message === "") {
        alert("Favor importar um arquivo")
        return
    }
    importFile(message)
}

button.click(readFile)
