const { Pact } = require("@pact-foundation/pact")
const path = require("path")
const client = require("../src/client.js")


describe('Pact with Order API', () => {

	const provider = new Pact({
	  port: 1234,
	  log: path.resolve(process.cwd(), "logs", "pact.log"),
	  dir: path.resolve(process.cwd(), "pacts"),
	  consumer: "OrderWeb",
	  provider: "OrderApi"
	});

  describe('given there are orders', () => {
    describe('when a call to the API is made', () => {
      beforeAll(done => {
        provider.setup()
	.then(() => {
		provider.addInteraction({
		  state: 'there are orders',
		  uponReceiving: 'a request for orders',
		  withRequest: {
		    path: '/orders',
		    method: 'GET',
		  },
		  willRespondWith: {
		    body: eachLike({
		      id: 1,
		      items: eachLike({
			name: 'burger',
			quantity: 2,
			value: 100,
		      }),
		    }),
		    status: 200,
		    headers: {
		      'Content-Type': 'application/json; charset=utf-8',
		    },
		  },
		})
		
		done()
		}
	)

      })

      it('will receive the list of current orders', () => {
        return expect(client.fetchOrders()).to.eventually.have.deep.members([
          new Order(orderProperties.id, [itemProperties]),
        ])
      })
    })
  })
})
