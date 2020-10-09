const request = require("request")

const fetchOrders = () => {
  return request.get(`http://localhost:1234/orders`).then(
    res => {
      return res.body.map((o) => {
        return new Order(o.id, o.items)
      })
    },
    err => {
      throw new Error(`Error from response: ${err.body}`)
    }
  )
}

exports.fetchOrders = fetchOrders
