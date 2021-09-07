const mongoose = require('mongoose');

const { Schema } = mongoose;

const reviewSchema = new Schema({
	parkID: mongoose.ObjectId,
	blurb: String,
	reviewer: String,
	title: String,
	review: String,
	stars: Number,
}, { collection: 'Reviews' });

mongoose.model('ReviewModel', reviewSchema);
