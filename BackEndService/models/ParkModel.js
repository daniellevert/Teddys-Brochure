const mongoose = require('mongoose');

const { Schema } = mongoose;

const parkSchema = new Schema({
	parkName: String,
	blurb: String,
	latdec: Number,
	longdec: Number,
	map: String,
	images: [
		String,
	],
}, { collection: 'NationalParks' });

mongoose.model('ParkModel', parkSchema);
