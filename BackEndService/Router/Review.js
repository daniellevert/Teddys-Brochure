const mongoose = require('mongoose');
const express = require('express');

const reviewRouter = express.Router();
const ReviewModel = mongoose.model('ReviewModel');

reviewRouter.use((req, res, next) => {
	next();
});

reviewRouter.get('/:id', async (req, res) => {
	const reviewId = req.params.id;

	if (!reviewId) {
		res.status(404).send({ error: 'No Park Is Found' });
		return;
	}

	const review = await ReviewModel.find({
		parkID: reviewId,
	});

	res.status(200).send(review);
});

reviewRouter.post('/:id', async (req, res) => {
	const reviewId = req.params.id;
	const {
		title, reviewer,
		review, stars,
	} = req.body;

	if (!reviewId) {
		res.status(404).send({ error: 'Review ID is Empty' });
		return;
	}

	if (!review || !reviewer || !title || !stars) {
		res.status(404).send({ error: 'No Comments Found' });
		return;
	}

	const reviewModel = new ReviewModel({
		parkID: reviewId,
		reviewer,
		review,
		title,
		stars,
	});

	try {
		await reviewModel.save();
	} catch (e) {
		res.status(404).send({ error: 'Back end issues' });
	}

	res.status(200).send(reviewModel);
});

module.exports = reviewRouter;
