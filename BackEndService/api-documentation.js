const express = require('express');
const bodyParser = require('body-parser');
const swaggerUI = require('swagger-ui-express');
const swaggerJSDoc = require('swagger-jsdoc');
const swaggerJson = require('./swagger.json');

// Set Up Global Variable
const app = express();
const PORT = 8080;

// Set Up Middle Ware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Main Route
app.use('/', swaggerUI.serve, swaggerUI.setup(swaggerJson));

// Open Listener
app.listen(PORT, () => {
	console.log(`Port Opened On ${PORT}`);
});
