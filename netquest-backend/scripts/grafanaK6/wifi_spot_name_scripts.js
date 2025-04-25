import http from 'k6/http'
import { check, sleep } from 'k6'
import encoding from 'k6/encoding'

//k6 run .\wifi_spot_name_scripts.js

var database = "mysql"

export let options = {
    vus: 1,
    iterations: '5',
}

export default function () {
    const username = 'admin'
    const password = 'admin'
    const credentials = `${username}:${password}`
    const encoded = encoding.b64encode(credentials)

    const res = http.put('http://localhost:8080/api/wifi-spot/'+database+'/name', null, {
        headers: {
            'Authorization': `Basic ${encoded}`
        }
    })

    check(res, {
        'status is 200': (r) => r.status === 200
    })

    sleep(0.3)
}
