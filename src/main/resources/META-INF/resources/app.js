const depotsTable = $('#depots')
const customerTable = $('#customer')
const vehicleTable = $('#vehicle')
const chooseButton = $('#chooseButton')

const fetchHeaders = {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
}

let body = null

const getList = () => {
    fetch('/vrp/list', fetchHeaders)
        .then((response) => {
            if (!response.ok) {
                alert("Error recuperando dados")
                return handleErrorResponse('Get status failed', response)
            } else {
                return response.json().then((data) => {
                    body = data
                    showLists(data)
                })
            }
        })
        .catch((error) => {
            alert("Error recuperando dados")
            handleClientError('Failed to process response', error)
        })
}

const create = (body) => {
    fetch('/vrp/create', {...fetchHeaders, method: 'POST', body: JSON.stringify(body).trim()})
        .then((response) => {
            if (!response.ok) {
                alert("Error criando rota, tente novamente")
                return handleErrorResponse('Post status failed', response)
            } else {
                window.location.replace("/routes/index.html");
            }
        })
        .catch((error) => {
            alert("Error criando rota, tente novamente")
            handleClientError('Failed to process response', error)
        })
}


const handleErrorResponse = (title, response) => {
    return response.text()
        .then((body) => {
            const message = `${title} (${response.status}: ${response.statusText}).`
            console.error(message, body)
        })
}

const handleClientError = (title, error) => {
    console.error(error)
}

getList()

const showLists = ({first, second, third}) => {
    customerTable.children().remove()
    first.forEach((customer) => {
        const {id, name, demand} = customer
        customerTable.append(`<tr>
      <td><i  id="crosshairs-${name}"
      style="display: inline-block width: 1rem height: 1rem text-align: center">
      </i></td><td><input type="checkbox" name="customer" value="${id}">
      <label for="${name}">${name} | Tons: ${demand}</label>
      </td>
      </tr>`)

    })

    depotsTable.children().remove()
    second.forEach((depot) => {
        const {id, name} = depot
        depotsTable.append(`<tr>
      <td><i  id="crosshairs-${name}"
      style="display: inline-block width: 1rem height: 1rem text-align: center">
      </i></td><td><input type="checkbox" name="depot" value="${id}">
      <label for="${name}">${name}</label>
      </td>
      </tr>`)
    })

    vehicleTable.children().remove()
    third.forEach((vehicle) => {
        const {id, plate, capacity} = vehicle
        vehicleTable.append(`<tr>
      <td><i id="crosshairs-${plate}"
      style="display: inline-block width: 1rem height: 1rem text-align: center">
      </i></td><td><input type="checkbox" name="vehicle" value="${id}">
      <label for="${plate}">Placa: ${plate}| Capacidade: ${capacity}</label>
      </td>
      </tr>`)

    })
}
let customerList = []
let depotList = []
let vehicleList = []

const filterArray = () => {
    const {first, second, third} = body

    create({
        first: first.filter(customer => customerList.includes(customer.id.toString())),
        second: second.filter(depot => depotList.includes(depot.id.toString())),
        third: third.filter(vehicle => vehicleList.includes(vehicle.id.toString()))
    })
}

const getIds = () => {
    $("input:checkbox[name=customer]:checked").each(function () {
        customerList.push($(this).val())
    })

    $("input:checkbox[name=vehicle]:checked").each(function () {
        vehicleList.push($(this).val())

    })

    $("input:checkbox[name=depot]:checked").each(function () {
        depotList.push($(this).val())
    })

    if (customerList.length <= 0 || depotList.length <= 0 || vehicleList.length <= 0) {
        alert("Favor selecionar ao menos um de cada")
        customerList = []
        depotList = []
        vehicleList = []
        return
    }
    filterArray()
}

chooseButton.click(getIds)
