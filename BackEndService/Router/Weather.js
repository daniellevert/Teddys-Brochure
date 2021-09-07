const express = require('express');
const axios = require('axios');

const { weatherAPIKey, weatherAPIUrl } = require('../config.js');

const weatherRouter = express.Router();

weatherRouter.use((req, res, next) => {
	// console.log('Pass Through Weather APi');
	next();
});

weatherRouter.get('/', (req, res) => {
	const { lat, long } = req.query;

	if (!lat && typeof parseInt(lat, 10) !== 'number') {
		res.status(404).send({ error: 'Lattitude Is Undefine' });
	} else if (!long && typeof parseInt(long, 10) !== 'number') {
		res.status(404).send({ error: 'Longtitude Is Undefine' });
	}

	axios.get(`${weatherAPIUrl}?lat=${lat}&lon=${long}&units=imperial&exclude=minutely,hourly,alerts&appid=${weatherAPIKey}`)
		.then((axiosRes) => {
			res.status(200).send(axiosRes.data);
		})
		.catch((axiosError) => {
			res.status(404).send(axiosError.data);
		});
});

module.exports = weatherRouter;
