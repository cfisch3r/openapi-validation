const axios = require('axios')
const adapter = require("axios/lib/adapters/http")

axios.defaults.adapter = adapter;

class API {

    constructor(url) {
        if (url.endsWith("/")) {
            url = url.substr(0, url.length - 1)
        }
        this.url = url
    }

    withPath(path) {
        if (!path.startsWith("/")) {
            path = "/" + path
        }
        return `${this.url}${path}`
    }

    async getPrice(bookIds) {
        return axios.get(this.withPath("/price"), {
            data: bookIds
        })
            .then(r => r.data);
    }

}

module.exports.API = API;
