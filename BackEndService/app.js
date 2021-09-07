// Externals Frameworks & Lib
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const path = require('path');
const mongoose = require('mongoose');

// Internal import
require('./models/ParkModel.js');
require('./models/ReviewModel.js');
const nationalParkRouter = require('./Router/NationalPark.js');
const weatherAPIRouter = require('./Router/Weather.js');
const ReviewRouter = require('./Router/Review.js');
const { mongodbKey } = require('./config.js');

// Set Up Global Variable
mongoose.Promise = global.Promise;
mongoose.connect(mongodbKey, { useNewUrlParser: true, useUnifiedTopology: true });
const app = express();
const PORT = 3000;

// Set Up Middle Ware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cors());

const dir = path.join(__dirname, 'images');
app.use('/images', express.static(dir));

// Use Router
app.use('/nationalPark', nationalParkRouter);
app.use('/weather', weatherAPIRouter);
app.use('/review', ReviewRouter);

app.listen(PORT, () => {
	console.log(`Main Application On Port ${PORT}`);
});
