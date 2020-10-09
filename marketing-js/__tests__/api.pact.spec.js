const path = require("path");
const {Pact} = require("@pact-foundation/pact");
const {API} = require("../src/api.js");
const {eachLike, like} = require("@pact-foundation/pact/dsl/matchers");

const provider = new Pact({
    consumer: 'Marketing',
    provider: 'PriceService',
    log: path.resolve(process.cwd(), 'logs', 'pact.log'),
    logLevel: "warn",
    dir: path.resolve(process.cwd(), 'pacts'),
    spec: 2
});

describe("API Pact test", () => {


    beforeAll(() => provider.setup());
    afterEach(() => provider.verify());
    afterAll(() => provider.finalize());

    describe("getting price", () => {
        test("price exists", async () => {

            // set up Pact interactions
            await provider.addInteraction({
                state: 'price exists',
                uponReceiving: 'get price',
                withRequest: {
                    method: 'GET',
                    path: '/price',
                    body: eachLike("I")
                },
                willRespondWith: {
                    status: 200,
                    headers: {
                        'Content-Type': 'application/json; charset=utf-8'
                    },
                    body: eachLike({
                        inCent: 1200,
                        tax: 43
                    }),
                },
            });

            const api = new API(provider.mockService.baseUrl);

            // make request to Pact mock server
            const product = await api.getPrice(["I"]);

            expect(product).toStrictEqual([
                {"inCent": 1200, "tax": 43}
            ]);
        })
    });
});
